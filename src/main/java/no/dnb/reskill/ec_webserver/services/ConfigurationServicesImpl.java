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
    public List<Configuration> getConfigurations() {
        return null;
    }

    @Override
    public List<Configuration> getConfigurationsByEnvironmentId(Long environmentId) {
        return null;
    }
}
