package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.models.Environment;

import java.util.List;

public interface EnvironmentService {
    public List<Environment> findAll();
    public Environment findById(Long id);
    public Environment updateDescriptionById(Long id, String description);


}
