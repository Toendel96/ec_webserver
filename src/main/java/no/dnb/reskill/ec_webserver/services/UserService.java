package no.dnb.reskill.ec_webserver.services;

import no.dnb.reskill.ec_webserver.models.User;

import java.util.List;

public interface UserService {
    public List<User> findAll();
    public User findById(String id);
    public User findByUsername(String username);

    boolean updateDescriptionById(String id, String description);
}