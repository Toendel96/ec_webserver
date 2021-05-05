package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.Environment;

import java.util.List;

public interface EnvironmentService {
    public List<Environment> findAll();
//    public Environment findById(Long id);
    public Environment findById(String id);
//    public Environment updateDescriptionById(Long id, String description);
    public Environment updateDescriptionById(String id, String description);
    public Environment addEnvironment(Environment environment);
//    public void deleteEnvironmentById(Long id);
    public void deleteEnvironmentById(String id);


}
