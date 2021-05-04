package no.dnb.reskill.ec_webserver.repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
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
    }


    //        jdbcTemplate.update("INSERT INTO User (id, username, password, user_type) VALUES (?,?,?,?)",
//                new Object[]{ 1L, "Salim","pass1", "Admin"});
//        jdbcTemplate.update("INSERT INTO User (id, username, password, user_type) VALUES (?,?,?,?)",
//                new Object[]{ 2L, "Petter","pass2", "Admin"});
//        jdbcTemplate.update("INSERT INTO User (id, username, password, user_type) VALUES (?,?,?,?)",
//                new Object[]{ 3L, "Sigbjørn","pass3", "Admin"});


}
