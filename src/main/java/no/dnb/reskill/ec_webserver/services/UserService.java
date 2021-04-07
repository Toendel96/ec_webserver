package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.models.User;

import java.util.List;

public interface UserService {
    // Which services should we offer for user?
    public List<User> findAll();
    public User findById(Long id);

    boolean updateDescriptionById(Long id, String description);
}