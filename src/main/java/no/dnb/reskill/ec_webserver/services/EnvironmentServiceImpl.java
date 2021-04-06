package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {
    // Varibel av type EnvironmentRepository

    // Contructor som autowire variabelen over (må autowires)


    @Override
    public List<Configuration> getConfigurations() {
        return null;
    }
}
