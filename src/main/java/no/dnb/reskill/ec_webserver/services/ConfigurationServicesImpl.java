package no.dnb.reskill.ec_webserver.services;


import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.repositories.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConfigurationServicesImpl implements ConfigurationService {
    private ConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationServicesImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
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
        return null;
        //TODO: Fix this
        // return (List<ConfigurationM>) configurationRepository.findByEnvironmentId(environmentId);
    }

    @Override
    public List<Configuration> findAllModifiedAfterDate(LocalDateTime date) {
        return null;
        //TODO: Fix this
        //return (List<ConfigurationM>) configurationRepository.findAllModifiedAfterDate(date);
    }
}
