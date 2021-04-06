package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.repositories.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationServicesImpl implements ConfigurationService {
    private ConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationServicesImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }


    // Implementation of interface methods
    @Override
    public List<Configuration> findAll() {
        return (List<Configuration>) configurationRepository.findAll();
    }

    @Override
    public List<Configuration> findByEnvironmentId(Long environmentId) {
        return (List<Configuration>) configurationRepository.findByEnvironmentId(environmentId);
    }

    @Override
    public Configuration findById(Long id) {
        return configurationRepository.findById(id).orElse(null);
    }
}
