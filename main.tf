# Source: https://klotzandrew.com/blog/deploy-an-ec2-to-run-docker-with-terraform

provider "aws" {
  region = var.aws_region
}

terraform {
  required_version = ">= 0.12.0"
}

data "aws_vpc" "default" {
  default = true
}

data "aws_subnet_ids" "all" {
  vpc_id = data.aws_vpc.default.id
}

resource "aws_ecr_repository" "s3p-app" {
  name = var.repository_backend
}

resource "aws_ecr_repository" "dynamodb" {
  name = var.repository_database
}


resource "aws_iam_instance_profile" "ec2_profile_s3p-app" {
  name = "ec2_profile_s3p-app"
  role = aws_iam_role.ec2_allow_ecr.name
}

resource "aws_iam_role" "ec2_allow_ecr" {
  name = "role_ec2_allow_ecr"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Effect": "Allow"
    }
  ]
}
EOF
}


resource "aws_iam_role_policy" "ec2_policy" {
  name = "ec2_policy"
  role = aws_iam_role.ec2_allow_ecr.id

  #TODO: "ecr:*" er ikke nødvendig. Ble lagt til som feilsøking, men begrenset tilgang hadde vært nok.
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "ecr:*",
      "Effect": "Allow",
      "Resource": "*"
    }
  ]
}
EOF
}

module "dev_ssh_sg" {
  source = "terraform-aws-modules/security-group/aws"
  name = "dev_ssh_sg"
  description = "Security group for dev_ssh_sg"
  vpc_id = data.aws_vpc.default.id
  ingress_cidr_blocks = ["0.0.0.0/0"]
  ingress_rules = ["ssh-tcp"]
}

resource "aws_security_group" "ec2_sg" {
  name = "ec2_sg"
  description = "Security group for EC2"
  vpc_id = data.aws_vpc.default.id

  ingress {
    description = "Database Admin over HTTP"
    from_port = var.port_database_admin
    to_port = var.port_database_admin
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Swagger UI over HTTP"
    from_port = var.port_backend_api
    to_port = var.port_backend_api
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

}

data "aws_ami" "amazon_linux_2" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-ebs"]
  }
}

resource "aws_instance" "web" {
  ami = data.aws_ami.amazon_linux_2.id
  instance_type = var.instance_type

  root_block_device {
    volume_size = 8
  }

  vpc_security_group_ids = [
    aws_security_group.ec2_sg.id,
    module.dev_ssh_sg.security_group_id
  ]

  iam_instance_profile = aws_iam_instance_profile.ec2_profile_s3p-app.name

  key_name = var.keyPairName
  monitoring = true
  disable_api_termination = false
  ebs_optimized = true

  provisioner "remote-exec" {
    inline = [
      # Docker # Source: https://docs.aws.amazon.com/AmazonECS/latest/developerguide/docker-basics.html
      "sudo yum update -y",
      "sudo amazon-linux-extras install docker -y",
      "sudo service docker start",
      "sudo usermod -a -G docker ${var.aws_ec2_username}",
      # Docker Compose # Source: https://docs.docker.com/compose/install/
      "sudo curl -L \"https://github.com/docker/compose/releases/download/1.29.1/docker-compose-$(uname -s)-$(uname -m)\" -o /usr/local/bin/docker-compose",
      "sudo chmod +x /usr/local/bin/docker-compose"
    ]
    # Login to the ec2-user with the aws key.
    # Source: https://brad-simonin.medium.com/learning-how-to-execute-a-bash-script-from-terraform-for-aws-b7fe513b6406
    connection {
      type        = "ssh"
      user        = var.aws_ec2_username
      password    = ""
      private_key = file(var.keyPath)
      host        = self.public_ip
      agent       = false
    }
  }
}


resource "null_resource" "login_and_push" {
  depends_on = [
    aws_instance.web,
  ]

  #TODO: put names/tags in variables
  #TODO: reuse parts of strings??? (local functions to create strings???)

  provisioner "local-exec" {
    command = "aws ecr get-login-password --region ${var.aws_region} | docker login --username AWS --password-stdin ${var.aws_account_number}.dkr.ecr.${var.aws_region}.amazonaws.com"
  }

  provisioner "local-exec" {
    command = "docker tag ${var.image_backend}:latest ${var.aws_account_number}.dkr.ecr.${var.aws_region}.amazonaws.com/${var.repository_backend}:latest"
  }

  provisioner "local-exec" {
    command = "docker tag ${var.image_database}:latest ${var.aws_account_number}.dkr.ecr.${var.aws_region}.amazonaws.com/${var.repository_database}:latest"
  }

  provisioner "local-exec" {
    command = "docker push ${var.aws_account_number}.dkr.ecr.${var.aws_region}.amazonaws.com/${var.repository_backend}:latest"
  }

  provisioner "local-exec" {
    command = "docker push ${var.aws_account_number}.dkr.ecr.${var.aws_region}.amazonaws.com/${var.repository_database}:latest"
  }

}

resource "null_resource" "docker_login_and_compose" {
  depends_on = [
    null_resource.login_and_push,
  ]

  provisioner "file" {
    source        = "../ec_webserver/docker-compose.yml"
    destination   = "/home/${var.aws_ec2_username}/docker-compose.yml"

    connection {
      type        = "ssh"
      user        = var.aws_ec2_username
      password    = ""
      private_key = file(var.keyPath)
      host        = aws_instance.web.public_ip
      agent       = false
    }
  }

  provisioner "file" {
    source        = "../ec_webserver/.env.prod"
    destination   = "/home/${var.aws_ec2_username}/.env.prod"

    connection {
      type        = "ssh"
      user        = var.aws_ec2_username
      password    = ""
      private_key = file(var.keyPath)
      host        = aws_instance.web.public_ip
      agent       = false
    }
  }

  provisioner "file" {
    source        = "../ec_webserver/env_vars_production"
    destination   = "/home/${var.aws_ec2_username}/env_vars_production"

    connection {
      type        = "ssh"
      user        = var.aws_ec2_username
      password    = ""
      private_key = file(var.keyPath)
      host        = aws_instance.web.public_ip
      agent       = false
    }
  }

  # Manually copying files over SSH:
  # scp -i "C:\Users\AB30961\.ssh\TerraformS3P.key" [source file] [target location]
  # target location = ec2-user@ec2-13-51-156-210.eu-north-1.compute.amazonaws.com:/home/ec2-user/terraform

  provisioner "remote-exec" {
    inline = [
      "aws ecr get-login-password --region ${var.aws_region} | docker login --username AWS --password-stdin ${var.aws_account_number}.dkr.ecr.${var.aws_region}.amazonaws.com",
      "docker-compose --env-file .env.prod up --detach",
    ]
    connection {
      type        = "ssh"
      user        = var.aws_ec2_username
      password    = ""
      private_key = file(var.keyPath)
      host        = aws_instance.web.public_ip
      agent       = false
    }

  }

}
