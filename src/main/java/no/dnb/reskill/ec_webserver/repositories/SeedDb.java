package no.dnb.reskill.ec_webserver.repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.models.Environment;
import no.dnb.reskill.ec_webserver.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
public class SeedDb {
    private DynamoDBMapper dynamoDBMapper;
    private AmazonDynamoDB amazonDynamoDB;
    private UserRepository userRepository;
    private EnvironmentRepository environmentRepository;
    private ConfigurationRepository configurationRepository;

    @Autowired
    public SeedDb(
            AmazonDynamoDB amazonDynamoDB,
            UserRepository userRepository,
            EnvironmentRepository environmentRepository,
            ConfigurationRepository configurationRepository
    ) throws InterruptedException {
        this.amazonDynamoDB = amazonDynamoDB;
        this.userRepository = userRepository;
        this.environmentRepository = environmentRepository;
        this.configurationRepository = configurationRepository;
    }


    @Bean
    @DependsOn({"amazonDynamoDB"})
    public void SeedDb() throws InterruptedException {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        // User Table
        CreateTableRequest ctr = dynamoDBMapper.generateCreateTableRequest(User.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        TableUtils.createTableIfNotExists(amazonDynamoDB, ctr);
        TableUtils.waitUntilActive(amazonDynamoDB, ctr.getTableName());

        // Configuration Table
        CreateTableRequest ctrConf = dynamoDBMapper.generateCreateTableRequest(Configuration.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        TableUtils.createTableIfNotExists(amazonDynamoDB, ctrConf);
        TableUtils.waitUntilActive(amazonDynamoDB, ctrConf.getTableName());



        seedUserTable();
        seedConfigurationTable();

    }

    @Bean
    @DependsOn({"amazonDynamoDB"})
//    @Lazy
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


    private void seedConfigurationTable() {
        this.configurationRepository.save(
            new Configuration("d726ee4b-125b-4571-8a32-a2482ff8eeb1", "Prod key 1", "key 1 value", "5b62a1da-95b0-4db5-9e7f-3b64dde15a06", "5b62a1da-95b0-4db5-9e7f-3b64dde15a07")
        );
        this.configurationRepository.save(
                new Configuration("d726ee4b-125b-4571-8a32-a2482ff8eeb2", "Prod key 2", "key 2 value", "5b62a1da-95b0-4db5-9e7f-3b64dde15a06", "5b62a1da-95b0-4db5-9e7f-3b64dde15a07")
        );
        this.configurationRepository.save(
                new Configuration("d726ee4b-125b-4571-8a32-a2482ff8eeb3", "Prod key 3", "key 3 value", "5b62a1da-95b0-4db5-9e7f-3b64dde15a06", "5b62a1da-95b0-4db5-9e7f-3b64dde15a07")
        );
        this.configurationRepository.save(
                new Configuration("d726ee4b-125b-4571-8a32-a2482ff8eeb4", "Prod key 4", "key 4 value", "5b62a1da-95b0-4db5-9e7f-3b64dde15a06", "5b62a1da-95b0-4db5-9e7f-3b64dde15a07")
        );

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
