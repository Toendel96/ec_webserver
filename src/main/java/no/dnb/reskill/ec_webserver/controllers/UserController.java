package no.dnb.reskill.ec_webserver.controllers;

import no.dnb.reskill.ec_webserver.models.Environment;
import no.dnb.reskill.ec_webserver.models.User;
import no.dnb.reskill.ec_webserver.services.ConfigurationService;
import no.dnb.reskill.ec_webserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/user")
@CrossOrigin

public class UserController {




    private UserService userService;
    private ConfigurationService configurationService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.configurationService = configurationService;
        }



        @GetMapping(
                value="/all",
                produces={"application/json", "application/xml"}
        )
     public ResponseEntity<Collection<User>> findAll(@PathVariable Long id) {
       Collection<User> user = userService.findAll();
       if ( user == null ) {
                 return ResponseEntity.notFound().build();
           }
       else
           return ResponseEntity.ok().body(user);
  }


    @GetMapping(value="/{Id}")
    public ResponseEntity<User> findById(@PathVariable Long Id) {
        User user = userService.findById(Id);
        if ( user == null ) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(user);
        }
    }
    
    //todo - Finish this method
    @PutMapping(
            value="/updateUserDescription/{id}",
            consumes={"application/json","application/xml"},
            produces={"application/json","application/xml"}
    )
    //public ResponseEntity<Void> updateDescriptionById(@PathVariable Long id, @RequestParam(value="description", required=true) String description) {
    public ResponseEntity<Void> updateDescriptionById(@PathVariable Long id, @RequestBody String description) {
        if (userService.updateDescriptionById(id, description) == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().build();
    }

    //todo - Finish this method
    @PostMapping (
            value="/addUser",
            consumes={"application/json","application/xml"},
            produces={"application/json","application/xml"}
    )
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User e = userService.addUser(user);
        Long id = e.getId();
        URI uri = URI.create("/"+id);
        return ResponseEntity.created(uri).body(e);
    }

    }


