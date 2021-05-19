# Infrastructure by Terraform (backend)

## Background

During reskill 2021 we had 3 assignments, all leading up to the 3rd and final 
assignment where we provisioned infrastructure in AWS, for both front- and 
backend. To do this we used the AWS Console and the AWS CLI as follows:   
- Backend: https://github.com/Toendel96/ec_webserver/blob/main/Readme_Backend_BuildAndDeployToECS.md
- Frontend:  https://github.com/ZiggyHowland/ec_webclient/blob/main/README.md

For the last week of reskill an investigation of provisioning infrastructure
through Terraform was done. (AWS CloudFormation was also looked into, but it 
seemed too heavy to get a grip around in only 5-6 days).

## Introduction

This article describes what is needed to get a Spring Boot Application
running on an AWS EC2 instance

### Our backend setup

Docker images are created for Backend database and backend application.
These are uploaded to `AWS ECR (Elastic Container Repository)`. EC2 instance 
are set up with SSH access, and we use terraform `remote-exec` provisioner to 
perform actions on the server after it is created (ex. installing docker,
authorization, etc.). We also use Terraform `local-exec` to perform local
scripts (f.ex tagging docker images and pushing them to ECR).

### Our frontend setup
Check the Readme_Terraform.md in the frontend github-project (link in background)


(Amplify: https://www.qloudx.com/aws-amplify-vs-s3-for-static-website-hosting/)

## Prerequisites
- Terraform CLI must be installed: https://www.terraform.io/downloads.html
- AWS CLI installed
  AWS ECS CLI installed
- An AWS account and an user with admin access
- AWS credentials saved on your localhost (run `aws configure` to verify or add your credentials)
- Cloned and updated GIT repo for backend: https://github.com/Toendel96/ec_webserver
- The following 4 files are not a part of the git repo, and must be added manually:

`.env`
```properties
BACKEND_IMAGE=assignment2_backend
DB_IMAGE=instructure/dynamo-local-admin
SERVER_PORT=8008
ENV_FILE=env_vars_development
```

`.env.prod` (to be copied to EC2 later on)
```properties
BACKEND_IMAGE=[aws_account_number].dkr.ecr.[aws_region].amazonaws.com/[repository]:latest
DB_IMAGE=[aws_account_number].dkr.ecr.[aws_region].amazonaws.com/[repository]:latest
SERVER_PORT=8008
ENV_FILE=env_vars_production
```

2 files: `env_var_development` and `env_var_production` (to be copied to EC2 later on)
```properties
AWS_ACCESS_KEY_ID=dummy
AWS_SECRET_ACCESS_KEY=dummy
AWS_REGION=eu-north-1
```
> AWS DynamoDB is running in a container, and the dummy keys are needed to connect 
> to the DB.

## 1: Create (and test) locally

### Creating images 
First I made sure application was working and running from containers 
locally. This was done previously - so no big deal, but the arising problem
was the later need to use the same image both locally and on the 
EC2-instance. 
>...the arising problem was the need to use the same image independent of environment

#### Environment variables
This article helped me a lot understanding more about using environment 
variables: https://vsupalov.com/docker-arg-env-variable-guide/ 

Our image is created based on `Dockerfile` when running `docker build -t assignment2_backend .`. 
>Two ENV-variables are included as default values when the container is started, if 
 not overriden.
```dockerfile
FROM maven:3.6-jdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-slim

ENV SERVER_PORT=8001
ENV AMAZON_DYNAMODB_ENDPOINT=http://dynamodb:8000

COPY --from=builder /app/target/*.jar /app.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
```
##### Inspecting image and listing runtime environment variables
After creating the image the command `docker inspect [image-id]` was very 
useful to understand which variables are stored and available for the container
at runtime.

The command `docker run assignment2_backend env` also came in handy when 
needing to list only the environment variables available for the container.

```
PATH=/opt/java/openjdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
HOSTNAME=abf36ca39229
LANG=en_US.UTF-8
LANGUAGE=en_US:en
LC_ALL=en_US.UTF-8
JAVA_VERSION=jdk-11.0.11+9
JAVA_HOME=/opt/java/openjdk
SERVER_PORT=8001
AMAZON_DYNAMODB_ENDPOINT=http://dynamodb:8000
HOME=/root
```
>Please note the two ENV-variables from the `Dockerfile` in the output.

I also tested adding f.ex *-e "SERVER_PORT=8008*
to the command: `docker run -e "SERVER_PORT=8008" assignment2_backend env`, and 
this shows how it's possible to override a container's environment variables.
```
...
HOSTNAME=52eba4c5f279
SERVER_PORT=8008
...
```
>Please note how the SERVER_PORT variable got overriden.


##### Passing ARG and setting ENV when building image

I had no need to pass variables at build time, since my main idea has been to 
create an image which is ready to be used in any environment.


##### Passing ENV when running container

I used Docker Compose to run the two containers:
- database
- spring boot application

###### Using .env file

An `.env` file in the same folder as our `docker-compose.yml` file will 
automatically be read when running `docker-compose up`.
>The values in the `.env` file are ONLY available inside the `docker-compose.yml` 
>file, and will NOT be passed on as environment variables for the container.

By including `--env-file [filename]` to the `docker-compose` statement, 
another file than `.env` will be used.

```yaml 
version: '3.7'
services:
  dynamodb:
    container_name: dynamodb_docker
    image: "${DB_IMAGE}"
    ports:
      - '8000:8000'
  s3p-app:
    container_name: s3p_backend
    image: "${BACKEND_IMAGE}"
    environment:
      - SERVER_PORT=${SERVER_PORT}
      - NO_NEED_FOR_THIS=but still included to show a concept
    env_file:
      - "${ENV_FILE}"
    ports:
      - "8111:${SERVER_PORT}"
```
> Note that `.env` is something else than the `env_file` value inside the
> docker-compose file. The `env_file` contains values passed into the container 
> as environment variables, togheter with the variables defined in the 
> `environment` part of the compose file.

Running `docker-compose config` using the above `docker-compose.yml` file 
in combination with our `.env` and `env_vars_development` file will have the
following output: 

```
services:
  dynamodb:
    container_name: dynamodb_docker
    image: instructure/dynamo-local-admin
    ports:
    - published: 8000
      target: 8000
  s3p-app:
    container_name: s3p_backend
    environment:
      AWS_ACCESS_KEY_ID: dummy
      AWS_REGION: eu-north-1
      AWS_SECRET_ACCESS_KEY: dummy
      NO_NEED_FOR_THIS: but still included to show a concept
      SERVER_PORT: '8008'
    image: assignment2_backend
    ports:
    - published: 8111
      target: 8008
version: '3.7'
``` 
> Note that all the values from the files `.env` and `env_vars_development`
> is showing in the above output.

`.env` file:
```properties   
BACKEND_IMAGE=assignment2_backend
DB_IMAGE=instructure/dynamo-local-admin
SERVER_PORT=8008
ENV_FILE=env_vars_development
```

`env_vars_development` file:
```properties
AWS_ACCESS_KEY_ID=dummy
AWS_SECRET_ACCESS_KEY=dummy
AWS_REGION=eu-north-1
```

### Running and testing the containers

By running `docker-compose up --detach` our containers will be started and 
run locally. Based on the above values the backend API can be accessed and 
tested through: http://localhost:8111/swagger-ui/ 

Alternative adding the environment file to the command 
`docker-compose --env-fil [filename] up --detach`  


## 2: Automated creation of AWS environment

The Terraform files `main.tf` and `variables.tf` contain all the scripts
needed to spin up an EC2-instance and related infrastructure.

> Variables varying between different AWS accounts and localhosts are managed within
> the Terraform file: `variables.tf`. Make sure these are correct for your environment
> before moving on.

After cloning this project, run `terraform init`, and if successful follow 
up by running `terraform apply` (which is the main command to setup and update
your infrastructure). 

When *'terraform apply'* is run, you need to confirm the operation by typing 
`yes` before the operation starts.

> While waiting for terraform to complete, you may log in to the Amazon console
> and go to the EC2 section to see the new instance appear.

> The Terraform script outputs the EC2 instance ID and IP at the end, and
> if you type the following command into your browser, you can verify that 
> server- and database containers are running:  
> `http://[ec2-instance-public-ip]:8111/swagger-ui/`

### Installing Docker on EC2

The terraform script will in addition to setting up the EC2-server also 
log in and install Docker and Docker Compose on the server through SSH.

This is done by setting up a `remote-exec` provisioner on the ec2-instance 
resource specification, as follows: 

```terraform
provisioner "remote-exec" {
    inline = [
      # Docker # Source: https://docs.aws.amazon.com/AmazonECS/latest/developerguide/docker-basics.html
      "sudo yum update -y",
      "sudo amazon-linux-extras install docker -y",
      "sudo service docker start",
      "sudo usermod -a -G docker ec2-user",
      # Docker Compose # Source: https://docs.docker.com/compose/install/
      "sudo curl -L \"https://github.com/docker/compose/releases/download/1.29.1/docker-compose-$(uname -s)-$(uname -m)\" -o /usr/local/bin/docker-compose",
      "sudo chmod +x /usr/local/bin/docker-compose"
    ]
    # Login to the ec2-user with the aws key.
    # Source: https://brad-simonin.medium.com/learning-how-to-execute-a-bash-script-from-terraform-for-aws-b7fe513b6406
    connection {
      type        = "ssh"
      user        = "ec2-user"
      password    = ""
      private_key = file(var.keyPath)
      host        = self.public_ip
      agent       = false
    }
  }
```

### Tagging and pushing images to AWS ECR

After setting up the EC2-instance and installing Docker the Terraform script
continues by tagging and pushing the two Docker images from localhost to 
the EC2-instance. 

This is done by setting up a "null_resource" to perform `local-exec` by 
giving it the command to run.

```terraform
resource "null_resource" "login_and_push" {
  depends_on = [
    aws_instance.web,
  ]
  provisioner "local-exec" {
    command = "aws ecr get-login-password --region eu-north-1 | docker login --username AWS --password-stdin ${var.aws_account_number}.dkr.ecr.eu-north-1.amazonaws.com"
  }
  provisioner "local-exec" {
    command = "docker tag assignment2_backend:latest ${var.aws_account_number}.dkr.ecr.eu-north-1.amazonaws.com/s3p-app:latest"
  }
  ... 
}
```


### Files copied to EC2

The Terraform script will copy the following 3 files from localhost
to the EC2-instance by using a `file` provisioner inside a "null_resource":
- `docker_compose.yml` (the compose file to be run)
- `.env.prod` (the production specific variables referenced inside docker_compose.yml)
- `env_vars_production` (environment variables needed by the docker container(s))


### Docker login to ECR (on EC2 instance)
> This is the same operation/command as done before tagging and pushing images/repositories
> from localhost.

The Terraform script runs the command which connects Docker on the EC2-instance 
with AWS ECR.

### Starting containers on server

The last thing the Terraform script does is running a `docker compose up` command 
with a `--detach` option, and including a pointer to the production environment file.

## 3: Removing (destroy) AWS environment

The command `terraform destroy` removes all AWS resources created on your account
by the Terraform script. This brings your account back to the starting point. 

