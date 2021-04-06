package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.repositories.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServicesImpl {
    private ConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationServicesImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    // Implementation of interface methods


}
