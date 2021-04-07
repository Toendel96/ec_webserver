package no.dnb.reskill.ec_webserver.controllers;

import no.dnb.reskill.ec_webserver.models.User;
import no.dnb.reskill.ec_webserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/user")
@CrossOrigin

public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        }

   @GetMapping(value="/all")
     public ResponseEntity<Collection<User>> findAll() {
       Collection<User> user = userService.findAll();
       if ( user == null ) {
                 return ResponseEntity.notFound().build();
           }
       else {
           return ResponseEntity.ok().body(user);
       }
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

    }


