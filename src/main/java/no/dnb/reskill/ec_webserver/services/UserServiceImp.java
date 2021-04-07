package no.dnb.reskill.ec_webserver.services;
import no.dnb.reskill.ec_webserver.models.User;
import no.dnb.reskill.ec_webserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImp (UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Implementation of interface methods
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }


    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}

