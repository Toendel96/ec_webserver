package no.dnb.reskill.ec_webserver.services;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.models.User;
import no.dnb.reskill.ec_webserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AmazonDynamoDB amazonDynamoDB) {
        this.userRepository = userRepository;
        this.amazonDynamoDB = amazonDynamoDB;
    }


    // Implementation of interface methods
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }


    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(username));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("username = :val1")
                .withExpressionAttributeValues(eav);

        try {
            return mapper.scan(User.class, scanExpression).stream().findFirst().orElse(null);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public boolean updateDescriptionById(String id, String description) {
        return false;
    }
}

