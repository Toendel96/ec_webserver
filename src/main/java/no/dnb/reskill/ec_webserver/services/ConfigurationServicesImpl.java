package no.dnb.reskill.ec_webserver.services;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.repositories.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationServicesImpl implements ConfigurationService {
    private ConfigurationRepository configurationRepository;
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    public ConfigurationServicesImpl(
            ConfigurationRepository configurationRepository,
            AmazonDynamoDB amazonDynamoDB
    ) {
        this.configurationRepository = configurationRepository;
        this.amazonDynamoDB = amazonDynamoDB;
    }

    @Override
    public List<Configuration> findAll() {
        return (List<Configuration>) configurationRepository.findAll();
    }

    @Override
    public Configuration findById(String id) {
        return configurationRepository.findById(id).orElse(null);
    }

    @Override
    public Configuration insertConfiguration(Configuration configuration) throws IllegalArgumentException {
        return configurationRepository.save(configuration);
    }

    @Override
    public Configuration updateConfiguration(Configuration configuration) throws IllegalArgumentException {
        return configurationRepository.save(configuration);
    }

    @Override
    public void deleteConfigurationById(String id) throws IllegalArgumentException {
        configurationRepository.deleteById(id);
    }


    @Override
    public List<Configuration> findByEnvironmentId(String environmentId) {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(environmentId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("environmentId = :val1")
                .withExpressionAttributeValues(eav);

        try {
            return mapper.scan(Configuration.class, scanExpression);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Configuration> findAllModifiedAfterDate(LocalDateTime date) {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

//        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDate = date.format(formatter);

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(formattedDate));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("timestamp_modified >= :val1")
                .withExpressionAttributeValues(eav);
        try {
            return mapper.scan(Configuration.class, scanExpression);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
