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
import org.springframework.stereotype.Component;


@Component
public class SeedDb {
    private final static String USER_ID_PETTER = "676d13bb-d760-4683-8687-72b246303854";
    private final static String USER_ID_SALIM = "5b62a1da-95b0-4db5-9e7f-3b64dde15a06";
    private final static String USER_ID_SIGBJORN = "209aee77-1f61-4fc5-859e-ea59a31f4bcb";
    private final static String USER_ID_SVETLANA = "d726ee4b-125b-4571-8a32-a2482ff8eeb1";

    private final static String ENV_ID_PROD = "env_seed_1";
    private final static String ENV_ID_STAGE = "env_seed_2";
    private final static String ENV_ID_ATEST = "env_seed_3";
    private final static String ENV_ID_STEST = "env_seed_4";
    private final static String ENV_ID_DEV = "env_seed_5";


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
        CreateTableRequest ctrUser = dynamoDBMapper.generateCreateTableRequest(User.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        TableUtils.createTableIfNotExists(amazonDynamoDB, ctrUser);
        TableUtils.waitUntilActive(amazonDynamoDB, ctrUser.getTableName());

        // Environment
        CreateTableRequest ctrEnvironment = dynamoDBMapper.generateCreateTableRequest(Environment.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        TableUtils.createTableIfNotExists(amazonDynamoDB, ctrEnvironment);
        TableUtils.waitUntilActive(amazonDynamoDB, ctrEnvironment.getTableName());

        // Configuration Table
        CreateTableRequest ctrConfiguration = dynamoDBMapper.generateCreateTableRequest(Configuration.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        TableUtils.createTableIfNotExists(amazonDynamoDB, ctrConfiguration);
        TableUtils.waitUntilActive(amazonDynamoDB, ctrConfiguration.getTableName());

        seedUserTable();
        seedEnvironmentTable();
        seedConfigurationTable();
    }


    private void seedUserTable() {
        this.userRepository.save(
            new User(USER_ID_SALIM,"Salim", "pass1", "Admin"));
        this.userRepository.save(
                new User(USER_ID_PETTER, "Petter", "pass2", "Admin"));
        this.userRepository.save(
                new User( USER_ID_SIGBJORN, "Sigbjørn", "pass3", "Admin"));
        this.userRepository.save(
                new User( USER_ID_SVETLANA, "Svetlana", "pass4", "Admin"));
    }


    private void seedEnvironmentTable() {
        this.environmentRepository.save(
                new Environment(ENV_ID_PROD,"PROD", "Production"));
        this.environmentRepository.save(
                new Environment(ENV_ID_STAGE,"STAGE", "Staging"));
        this.environmentRepository.save(
                new Environment(ENV_ID_ATEST,"A-TEST", "Acceptance testing"));
        this.environmentRepository.save(
                new Environment(ENV_ID_STEST,"S-TEST", "Integration testing"));
        this.environmentRepository.save(
                new Environment(ENV_ID_DEV,"DEV", "Development"));
    }


    private void seedConfigurationTable() {
        this.configurationRepository.save(
                new Configuration("config_seed_p1", ENV_ID_PROD, "Database IP", "143.23.43.123", USER_ID_PETTER)
        );
        this.configurationRepository.save(
                new Configuration("config_seed_p2", ENV_ID_PROD, "Max amount users", "10.000", USER_ID_SALIM)
        );
        this.configurationRepository.save(
                new Configuration("config_seed_p3", ENV_ID_PROD, "Next audit date", "25.3.2023", USER_ID_SALIM)
        );
        this.configurationRepository.save(
                new Configuration("config_seed_p4", ENV_ID_PROD, "Responsible owner", "Glenn Medeiros", USER_ID_SVETLANA)
        );
        this.configurationRepository.save(
                new Configuration("config_seed_at1", ENV_ID_ATEST, "Max amount users", "650", USER_ID_SALIM)
        );
        this.configurationRepository.save(
                new Configuration("config_seed_at2", ENV_ID_ATEST, "Max latency allowed", "5300ms", USER_ID_SALIM)
        );
        this.configurationRepository.save(
                new Configuration("config_seed_at3", ENV_ID_ATEST, "Responsible owner", "Håkan Hellstrøm", USER_ID_SVETLANA)
        );

        this.configurationRepository.save(
                new Configuration("config_seed_d1", ENV_ID_DEV, "Database IP", "127.0.0.1", USER_ID_PETTER)
        );
        this.configurationRepository.save(
                new Configuration("config_seed_d2", ENV_ID_DEV, "Next audit date", "25.3.2025", USER_ID_SALIM)
        );
    }




}
