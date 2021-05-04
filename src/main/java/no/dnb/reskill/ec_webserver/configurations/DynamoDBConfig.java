package no.dnb.reskill.ec_webserver.configurations;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Primary;


@Configuration
@EnableDynamoDBRepositories(basePackages = "no.dnb.reskill.ec_webserver.repositories")
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

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



//    @Bean
//    public AWSCredentials awsCredentials() {
//        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
//    }


//

//

//
//    @Bean
//    public DynamoDBMapperConfig dynamoDBMapperConfig() {
//        return DynamoDBMapperConfig.DEFAULT;
//    }
//    @Bean
//    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
//        return new DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT);
//    }
//
//
//    @Bean
//    public AmazonDynamoDB amazonDynamoDB() {
//        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, "eu-west-1");
//        return AmazonDynamoDBClientBuilder
//                .standard()
//                .withEndpointConfiguration(endpoint)
//                .withCredentials(amazonAWSCredentialsProvider())
//                .build();
//    }



}