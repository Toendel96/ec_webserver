package no.dnb.reskill.ec_webserver.controllers;

import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.models.Environment;
import no.dnb.reskill.ec_webserver.services.ConfigurationService;
import no.dnb.reskill.ec_webserver.services.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/environments")
@CrossOrigin //Enable access for third parties
public class EnvironmentController {
    //Kaller først på EnvironmentServceImpl.findById
    //Deretter findByEnvironmentId i ConfigurationServiceImpl kan kalles her, og ikke fra EnvironmentServiceImpl

    private EnvironmentService environmentService;
    private ConfigurationService configurationService;

    @Autowired
    public EnvironmentController (EnvironmentService environmentService, ConfigurationService configurationService) {
        this.environmentService = environmentService;
        this.configurationService = configurationService;
    }

    // Endpoints listed here
    @GetMapping(value="/all")
    public ResponseEntity<Collection<Environment>> findAll() {
        Collection<Environment> environments = environmentService.findAll();
        if (environments == null ) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(environments);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Environment> findById(@PathVariable Long id) {
        Environment environment = environmentService.findById(id);
        if (environment == null ) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(environment);
    }

    //Remove same method from ConfigurationController?
    @GetMapping(path="/all/{environmentId}/configurations")
    public ResponseEntity<Collection<Configuration>> findEnvironmentAndBelongingConfigurationsByEnvironmentId(@PathVariable Long environmentId) {
        //Collection<Environment> environments = environmentService.findAll();
        Collection<Configuration> configurations = configurationService.findByEnvironmentId(environmentId);
        if ( configurations == null )return ResponseEntity.notFound().build();
         else return ResponseEntity.ok().body(configurations);
    }

    //todo - Finish this method
    @PutMapping(value="/updateEnvironmentDescription/{id}")
    public ResponseEntity<Void> updateDescriptionById(@PathVariable Long id, @RequestParam(value="description", required=true) String description) {
        if (environmentService.updateDescriptionById(id, description) == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().build();
    }

    //todo - Finish this method
    @PostMapping (value="/addEnvironment")
    public ResponseEntity<Environment> addEnvironment(@RequestBody Environment environment) {
        environmentService.addEnvironment(environment);
        Long id = environment.getId();
        URI uri = URI.create("/"+id);
        return ResponseEntity.created(uri).body(environment);
    }

}
