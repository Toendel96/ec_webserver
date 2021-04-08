package no.dnb.reskill.ec_webserver.controllers;

import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/configurations")
@CrossOrigin
public class ConfigurationController {
    private ConfigurationService configurationService;

    @Autowired
    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    // Endpoints listed here
    @GetMapping(value="/all")
    public ResponseEntity<Collection<Configuration>> findAll() {
        Collection<Configuration> configurations = configurationService.findAll();
        if ( configurations == null ) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(configurations);
        }
    }

    //Removing this later
//    @GetMapping(value="/byEnvironmentId/{environmentId}")
//    public ResponseEntity<Collection<Configuration>> findByEnvironmentId(@PathVariable Long environmentId) {
//        Collection<Configuration> configurations = configurationService.findByEnvironmentId(environmentId);
//        if ( configurations == null ) {
//            return ResponseEntity.notFound().build();
//        }
//        else {
//            return ResponseEntity.ok().body(configurations);
//        }
//    }

    //@GetMapping(value="/byId/{id}")
    @GetMapping(value="/{id}")
    public ResponseEntity<Configuration> findById(@PathVariable Long id) {
        Configuration configuration = configurationService.findById(id);
        if ( configuration == null ) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(configuration);
        }
    }


}
