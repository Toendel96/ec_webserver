package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.Configuration;

import java.time.LocalDateTime;
import java.util.List;

public interface ConfigurationService {
    // Which services should we offer for configuration?
    public List<Configuration> findAll();
    public List<Configuration> findByEnvironmentId(String environmentId);
    public Configuration findById(String id);
    public List<Configuration> findAllModifiedAfterDate(LocalDateTime date);
    public Configuration insertConfiguration(Configuration configuration);
    public Configuration updateConfiguration(Configuration configuration);
    public void deleteConfigurationById(String id);

//    public Iterable<Configuration> findByEnvironmentId(String environmentId);
//    Iterable<Configuration> findAllModifiedAfterDate(LocalDateTime date);


}
