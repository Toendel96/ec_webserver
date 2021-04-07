package no.dnb.reskill.ec_webserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String helloWorld(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello " + name + "!";
    }
}
