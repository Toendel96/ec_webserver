# ----------------- AWS settings

variable "instance_type" {
  description = "Type of EC2-instance"
  type = string
  default = "t3.micro"
}

variable "aws_region" {
  default = "eu-north-1"
}

variable "aws_account_number" {
  default = "873536720862"
}

variable "aws_ec2_username" {
  default = "ec2-user"
}



# ----------------- Localhost settings

variable "keyPath" {
  default = "C:/Users/AB30961/.ssh/ec2_terraform.pem"
}

variable "keyPairName" {
  default = "ec2_terraform"
}


# ----------------- App settings

variable "image_backend" {
  default = "assignment2_backend"
}

variable "image_database" {
  default = "instructure/dynamo-local-admin"
}

variable "repository_backend" {
  default = "s3p-app"
}

variable "repository_database" {
  default = "dynamodb"
}

variable "port_backend_api" {
  default = 8111
}

variable "port_database_admin" {
  default = 8000
}