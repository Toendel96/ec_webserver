package no.dnb.reskill.ec_webserver.configurations;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
@EnableDynamoDBRepositories(basePackages = "no.dnb.reskill.ec_webserver.repositories")
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws.access.key.id}")
    private String amazonKey;

    @Bean("amazonDynamoDB")
    public AmazonDynamoDB amazonDynamoDB() {
        System.out.println("DynamoDB endpoint: " + amazonDynamoDBEndpoint + "-------------------------");
        System.out.println("Access key: " + amazonKey + "---------------------------");

        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, "eu-west-1");
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(endpoint)
//                .withCredentials(amazonAWSCredentialsProvider())
                .build();
    }
}