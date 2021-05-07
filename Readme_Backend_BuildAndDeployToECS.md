# Build and deploy backend server to ECS

## Preparations

1. AWS CLI must be installed


2. Set up AWS user credentials by running the AWS CLI command
   `aws configuration`


3. ECS CLI must be installed (https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ECS_CLI_installation.html )


4. Allow docker to communicate with Amazon ECS:  
`aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin [AWS account]`  
   [AWS account] = 244530008913.dkr.ecr.eu-west-1.amazonaws.com  
  Source: https://docs.amazonaws.cn/en_us/AmazonECR/latest/userguide/getting-started-cli.html
   
## Build a container and sync with ECS repository

### Create Dockerfile

A `Dockerfile` must exist in the root-folder of your project 
   (the same as where the pom.xml is)
   
```dockerfile
# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.6-jdk-11 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
# https://hub.docker.com/r/adoptopenjdk/openjdk8
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM adoptopenjdk/openjdk11:alpine-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/*.jar /app.jar

# Run the web service on container startup.
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
```

### Build Docker image
Run the command:  
`docker build -t assignment2_backend .`  
>"*assignment2_backend*" = name of your container

Test the image by running the container locally, with the command:  
`docker run -p 8111:8111 assignment2_backend`
>"*8111:8111*" = localhost_port:container_port

### Create ECS repository
Your docker image can be stored in a repository on AWS ECS, and to 
enable that you must create a repository on ECS. 

Run the command:  
`aws ecr create-repository --repository-name group-s3p-backendserver`  
> "group-s3p-backendserver" = name of the repository 

>The **API response**  will be amongst others, an URI to the repo, looking like: 
>`244530008913.dkr.ecr.eu-west-1.amazonaws.com/[name of your repository]`

### Tag your local image
Tag your image so you can push the image to the AWS ECS repository
by running the command:  
`docker tag assignment2_backend:latest 
244530008913.dkr.ecr.eu-west-1.amazonaws.com/group-s3p-backendserver:latest`

> "*assignment2_backend:latest*" = localhost source image  
> "*group-s3p-backendserver:latest*" = cloud target image

Test the local container with the new tag with the command:    
`docker run -p 8111:8111 244530008913.dkr.ecr.eu-west-1.amazonaws.com/group-s3p-backendserver:latest`


### Push the image to AWS
You upload your docker image to AWS by running the following command:  
`docker push 244530008913.dkr.ecr.eu-west-1.amazonaws.com/group-s3p-backendserver:latest`


## Deploy a container using AWS Fargate

### yml-files for composer

You need two files placed in the root-folder of your project to continue.  
`docker-compose.yml` 
```yaml
version: '3'
services:
  web:
    image: 244530008913.dkr.ecr.eu-west-1.amazonaws.com/group-s3p-backendserver:latest
    ports:
      - "8111:8111"
    logging:
      driver: awslogs
      options: 
        awslogs-group: reskill_group_sps
        awslogs-region: eu-west-1
        awslogs-stream-prefix: web
```
> the 'image' property must be aligned with the image you pushed to repository

`ecs-params.yml`
```yaml
version: 1
task_definition:
  task_execution_role: ecsTaskExecutionRole
  ecs_network_mode: awsvpc
  task_size:
    mem_limit: 0.5GB
    cpu_limit: 256
run_params:
  network_configuration:
    awsvpc_configuration:
      subnets:
        - "subnet-59d44a03"
        - "subnet-a07854e8"
      security_groups:
        - "sg-062794537aa93e725"
      assign_public_ip: ENABLED
```
> The 'task_execution_role', 'subnets' and 'security_groups' are 
> related to your aws account. 

### Create a configuration for your ECS cluster
Run the following command to create a cluster configuration:   
`ecs-cli configure --cluster group-s3p --default-launch-type FARGATE --region eu-west-1 --config-name assignment3`

> "group-s3p" = the name you will give your cluster  
> "assignment3" = the name of this configuration (to be 
> referenced to in later commands)

### Create ECS cluster
Run the following command to create the cluster:  
`ecs-cli up --cluster-config assignment3`


### Deploy your Docker container to cluster
Run the following command to deploy the container and make it public available on AWS:  
`ecs-cli compose --project-name group_s3p_project service up --create-log-groups --cluster-config assignment3`

> "group_s3p_project" = The name of this container (???) when it's running

(This command uses the `docker-compose.yml` and the `ecs-params.yml` files to start
the container properly.)


### Other commands:
- View your running containers:  
`ecs-cli compose --project-name group_s3p_project service ps --cluster-config assignment3`
  
- Show logs from container/task:  
`ecs-cli logs --task-id [task_id] --follow --cluster-config assignment3`

- Scaling up/down:


### Extra things to keep in mind: 
- Remember 

