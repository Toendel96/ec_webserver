package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.Configuration;

import java.util.List;

public interface ConfigurationService {
    // Which services should we offer for configuration?
    public List<Configuration> getConfigurations();
    public List<Configuration> getConfigurationsByEnvironmentId(Long environmentId);


}
