package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.Environment;
import no.dnb.reskill.ec_webserver.repositories.EnvironmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {
    //@Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    public EnvironmentServiceImpl (EnvironmentRepository environmentRepository) {
        this.environmentRepository = environmentRepository;
    }

    @Override
    public List<Environment> findAll() {
        return (List<Environment>) environmentRepository.findAll();
    }

    @Override
    public Environment findById(Long id) {
        return environmentRepository.findById(id).orElse(null);
    }

    @Override
    public Environment updateDescriptionById(Long id, String description) {
        Environment environment = findById(id);
        if (environment != null) {
            environment.setDescription(description);
            return environmentRepository.save(environment); //Returns the saved entity;
        } else return null;
    }

    @Override
    public boolean addEnvironment(Environment environment) {
        Environment environment1 = findById(environment.getId());
        if (environment1 == null) {
            environmentRepository.save(environment);
            return true;
        } else return false;
    }


}
