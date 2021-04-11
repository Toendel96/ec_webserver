package no.dnb.reskill.ec_webserver.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.models.Environment;
import no.dnb.reskill.ec_webserver.models.Summary;
import no.dnb.reskill.ec_webserver.models.User;
import no.dnb.reskill.ec_webserver.services.ConfigurationService;
import no.dnb.reskill.ec_webserver.services.EnvironmentService;
import no.dnb.reskill.ec_webserver.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class TestController {

    private EnvironmentService es;
    private ConfigurationService cs;
    private UserService us;

    public TestController(EnvironmentService es, ConfigurationService cs, UserService us) {
        this.es = es;
        this.cs = cs;
        this.us = us;

    }


    @GetMapping(
            value="/summary",
            produces={"application/json", "application/xml"})
    public ResponseEntity<Collection<Summary>> analyzeAndCreateSummary() {
        Collection<Summary> summary = new ArrayList<>();

        Collection<Environment> ecol = es.findAll();
        if (ecol.size() > 0) {
            summary.add(new Summary("Number of environments", ecol.size()));

            Collection<Configuration> ccol = cs.findAll();
            if (ccol.size() > 0) {
                summary.add(new Summary("Total number of configurations:", ccol.size()));

                for (Environment e: ecol) {
                    ccol = cs.findByEnvironmentId(e.getId());
                    if (ccol.size() > 0) {
                        summary.add(new Summary(String.format("Number of configurations for '%s'", e.getShort_name()), ccol.size()));
                    }
                }
            }
        }
        else {
            summary.add(new Summary("No environment or related configurations available", 0));
        }

        Collection<User> ucol = us.findAll();
        if (ucol.size() > 0) {
            summary.add(new Summary("\"Number of admin users registered", ucol.size()));
        }

        if (summary.size() > 0) {
            return ResponseEntity.ok().body(summary);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }


    @GetMapping("/hello")
    public String helloWorld(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello " + name + "!";
    }

    @PostMapping("/user")
    public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
        //validate username/password

        String token = getJWTToken(username);
        User user = new User();
        user.setUsername(username);
        user.setToken(token);
        return user;
    }

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts
                .builder()
                .setId("reskillDNB")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+600_000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
        return "Bearer " + token;
    }


}
