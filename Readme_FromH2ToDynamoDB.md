# Moving from H2 to DynamoDB (local)

## DynamoDB/Docker
### Alternative 1: DynamoDB without GUI
Follow the instructions on Docker Hub to pull and run a 
docker-container with AWS DynamoDB:
https://hub.docker.com/r/amazon/dynamodb-local  

### Alternative 2: DynamoDB with simple admin GUI
Place the following `docker-compose.yml` file in an empty folder: 
```
services:
  dynamodb-local-admin-gui:
    container_name: dynamodb-local-admin-gui
    image: instructure/dynamo-local-admin
    ports:
      - '8000:8000'
version: '3.7'
```
Run the `docker compose up` command, and a simple DynamoDB admin GUI
will be available from `http://localhost:8000`.

Source: https://examples.javacodegeeks.com/spring-boot-crud-with-aws-dynamodb/


### Alternative 3: Amazon NoSQL Workbench
Not tested yet, but seems pretty neat: 
https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.html



## Changing dependencies
In the `pom.xml` file we **added** the following dependencies:
```
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk-dynamodb</artifactId>
    <version>1.11.1009</version>
</dependency>
<dependency>
    <groupId>com.github.derjust</groupId>
    <artifactId>spring-data-dynamodb</artifactId>
    <version>5.1.0</version>
</dependency>
```
And to stop jpa auto configuration we also **removed** the following
dependencies (including the H2 which we no longer need): 
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Application properties

In the `application.properties`, remove all related to H2 and data
source, and keep the following:
```
amazon.dynamodb.endpoint=http://localhost:8000/
amazon.aws.accesskey=key
amazon.aws.secretkey=key2
```
(Local DynamoDB does not use keys, so no values are needed)

> Do **NOT** place your AWS access- or secret keys in code or files
> where they can be publicly exposed.


## DynamoDB Configuration
Create a configuration file and use the `@Configuration` annotation to
make sure this is handled by Spring Boot.
```java
@Configuration
@EnableDynamoDBRepositories(basePackages = "no.dnb.reskill.ec_webserver.repositories")
public class DynamoDBConfig {
    
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    // TODO: Find another way to handle this
    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;


    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean("amazonDynamoDB")
    public AmazonDynamoDB amazonDynamoDB() {
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, "eu-west-1");
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(endpoint)
                .withCredentials(amazonAWSCredentialsProvider())
                .build();
    }
}
```


## Repository files
Removed JPA-related imports and functionality, and replace 
the annotation `@Repository` with `@EnableScan`.<br>
Changed from `<Configuration, Long>` to `<Configuration, String>`
- Best practice with NoSQL Hashkey is to use String
```java
@EnableScan
public interface ConfigurationRepository extends CrudRepository<Configuration, String> {
}
```

## Model files
These are the files we changed the most. All JPA annotations related to
database needed to be changed to AWS DynamoDB syntax.
Good source on AWS annotations:
https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.Annotations.html

`User.class`
```java 
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "User")
public class User {
    private Long id;
    private String username;
    private String password;
    private String user_type;
    private String token;

    public User(Long id, String username, String password, String user_type) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.user_type = user_type;
    }

    @DynamoDBHashKey(attributeName="id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @DynamoDBAttribute
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBAttribute
    public String getUser_type() {
        return user_type;
    }
    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @DynamoDBIgnore
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
```

## Seeding database

The `SeedDb` class is completely different, and while hibernate
automagically creates tables, we must use a `dynamoDBMapper` to create
tables.

The seeding of data into the table is straight forward by: 
1) Creating an object which one want to save
2) Handle the object in the repository

```java 
@Component
public class SeedDb {
    private DynamoDBMapper dynamoDBMapper;
    private AmazonDynamoDB amazonDynamoDB;
    private UserRepository userRepository;

    @Autowired
    public SeedDb(AmazonDynamoDB amazonDynamoDB, UserRepository userRepository) throws InterruptedException {
        this.amazonDynamoDB = amazonDynamoDB;
        this.userRepository = userRepository;
    }

    @Bean
    @DependsOn({"amazonDynamoDB"})
    public void SeedDb() throws InterruptedException {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        CreateTableRequest ctr = dynamoDBMapper.generateCreateTableRequest(User.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        TableUtils.createTableIfNotExists(amazonDynamoDB, ctr);
        TableUtils.waitUntilActive(amazonDynamoDB, ctr.getTableName());
        
        seedUserTable();
    }

    private void seedUserTable() {
        this.userRepository.save(
            new User(1L, "Salim", "pass1", "Admin"));
        this.userRepository.save(
                new User(2L, "Petter", "pass2", "Admin"));
        this.userRepository.save(
                new User(3L, "Sigbjørn", "pass3", "Admin"));
        this.userRepository.save(
                new User(4L, "Svetlana", "pass4", "Admin"));
    }
}
```
Info about the `@DependsOn` annotation:
https://www.baeldung.com/spring-depends-on 



## Related files
Test files, controllers and services mostly don't get affected by
the change, but due to removing a few JPA-dependent queries from the
repository files, needed adjustment. Some examples: 
- `ConfigurationServiceImpl.findByEnvironmentId()`: The function is 
  disabled since the JPA query in the repository is removed.
- `ConfigurationController.insertConfiguration()`: One line is commented
  since the related function is not available.
  


# Failures along the way...
## Multirepo
Tried to make H2 and DybnamoDB work together, as 2
different data sources - based on the following
article: https://github.com/derjust/spring-data-dynamodb-examples/blob/master/README-multirepo.md

## AWS access keys on GitHub...
Almost made "bad guys" hack Glenn's AWS account.<br>
Best practice is to remove keys from the code, and `configure environment variables` when connecting to DynamoDB from local Spring Boot server. 
1) Run
2) Edit Configurations
3) Add `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` with values to environment variables
4) Apply and ok


# Useful sources
