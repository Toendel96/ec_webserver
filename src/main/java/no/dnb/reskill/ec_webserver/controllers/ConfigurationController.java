package no.dnb.reskill.ec_webserver.controllers;

import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.models.Environment;
import no.dnb.reskill.ec_webserver.models.User;
import no.dnb.reskill.ec_webserver.services.ConfigurationService;
import no.dnb.reskill.ec_webserver.services.EnvironmentService;
import no.dnb.reskill.ec_webserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;


@RestController
@RequestMapping("/configurations")
@CrossOrigin
public class ConfigurationController {
    private ConfigurationService configurationService;
    private EnvironmentService environmentService;
    private UserService userService;

    @Autowired
    public ConfigurationController(
            ConfigurationService configurationService,
            EnvironmentService environmentService,
            UserService userService) {
        this.configurationService = configurationService;
        this.environmentService = environmentService;
        this.userService = userService;
    }

    @GetMapping(
            value="",
            produces={"application/json", "application/xml"}
    )
    public ResponseEntity<Collection<Configuration>> findAll(
            @RequestParam(name="modifiedAfterDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // 2021-04-08T23:32
            LocalDateTime date)
    {
        Collection<Configuration> configurations;
        if ( date != null ) {
            configurations = configurationService.findAllModifiedAfterDate(date);
        }
        else {
            configurations = configurationService.findAll();
        }


        if ( configurations == null ) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(configurations);
        }
    }

    @GetMapping(
            value="/{id}",
            produces={"application/json", "application/xml"}
    )
    public ResponseEntity<Configuration> findById(@PathVariable String id) {
        Configuration configuration = configurationService.findById(id);
        if ( configuration == null ) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(configuration);
        }
    }


    @PostMapping(
            path="",
            consumes={"application/json", "application/xml"},
            produces={"application/json", "application/xml"}
            )
    public ResponseEntity<Configuration> insertConfiguration(
            @RequestParam(name="environmentId", required = true) String environmentId,
            @RequestParam(name="userId", required = true) String userId,
            @RequestBody Configuration configuration ) {
        Configuration insertedConfiguration;
        Environment e = environmentService.findById(environmentId);
        User u = userService.findById(userId);

        if (e != null && u != null) {
            //configuration.setEnvironment(e); todo - denne feilet plutselig
            //configuration.setUser(u); TODO: Removed in order to test

            insertedConfiguration = configurationService.insertConfiguration(configuration);
            URI uri = URI.create("/configurations/" + insertedConfiguration.getId());
            return ResponseEntity.created(uri).body(insertedConfiguration);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping(
            path="/{id}",
            consumes={"application/json", "application/xml"},
            produces={"application/json", "application/xml"}
    )
    public ResponseEntity<Configuration> updateConfiguration(
            @PathVariable String id,
            @RequestBody Configuration configuration) {
        Configuration existingConfiguration = configurationService.findById(id);
        if (existingConfiguration != null && configuration.getId() == id) {
//            configuration.setEnvironment(existingConfiguration.getEnvironment());
//            configuration.setUserId(existingConfiguration.getUserId());
            configurationService.updateConfiguration(configuration);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(path={"/{id}"})
    public ResponseEntity<Void> deleteConfigurationById(
            @PathVariable String id) {
        try {
            configurationService.deleteConfigurationById(id);
            return  ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }


}
