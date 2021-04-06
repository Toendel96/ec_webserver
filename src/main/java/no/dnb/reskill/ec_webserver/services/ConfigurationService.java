package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.Configuration;

import java.util.List;

public interface ConfigurationService {
    // Which services should we offer for configuration?
    public List<Configuration> findAll();
    public List<Configuration> findByEnvironmentId(Long environmentId);
    public Configuration findById(Long id);


}
