package no.dnb.reskill.ec_webserver.repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import no.dnb.reskill.ec_webserver.models.Environment;
import no.dnb.reskill.ec_webserver.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


@Component
public class SeedDb {
    private DynamoDBMapper dynamoDBMapper;
    private AmazonDynamoDB amazonDynamoDB;
    private UserRepository userRepository;
    private EnvironmentRepository environmentRepository;

    @Autowired
    public SeedDb(AmazonDynamoDB amazonDynamoDB, UserRepository userRepository, EnvironmentRepository environmentRepository) throws InterruptedException {
        this.amazonDynamoDB = amazonDynamoDB;
        this.userRepository = userRepository;
        this.environmentRepository = environmentRepository;
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

    @Bean
    @DependsOn({"amazonDynamoDB"})
    public void SeedDbEnvironment() throws InterruptedException {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        CreateTableRequest ctr = dynamoDBMapper.generateCreateTableRequest(Environment.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        TableUtils.createTableIfNotExists(amazonDynamoDB, ctr);
        TableUtils.waitUntilActive(amazonDynamoDB, ctr.getTableName());

        seedEnvironmentTable();
    }

    private void seedUserTable() {
        this.userRepository.save(
            new User("5b62a1da-95b0-4db5-9e7f-3b64dde15a06","Salim", "pass1", "Admin"));
        this.userRepository.save(
                new User("676d13bb-d760-4683-8687-72b246303854", "Petter", "pass2", "Admin"));
        this.userRepository.save(
                new User( "209aee77-1f61-4fc5-859e-ea59a31f4bcb", "Sigbjørn", "pass3", "Admin"));
        this.userRepository.save(
                new User( "d726ee4b-125b-4571-8a32-a2482ff8eeb1", "Svetlana", "pass4", "Admin"));
    }


    private void seedEnvironmentTable() {
        this.environmentRepository.save(
                new Environment("5b62a1da-95b0-4db5-9e7f-3b64dde15a07","PROD", "Production"));
        this.environmentRepository.save(
                new Environment("5b62a1da-95b0-4db5-9e7f-3b64dde15a08","ENV", "Environment"));
        this.environmentRepository.save(
                new Environment("5b62a1da-95b0-4db5-9e7f-3b64dde15a09","Test", "Testing"));
        this.environmentRepository.save(
                new Environment("5b62a1da-95b0-4db5-9e7f-3b64dde15a10","Conf", "Configurations"));
    }

}
