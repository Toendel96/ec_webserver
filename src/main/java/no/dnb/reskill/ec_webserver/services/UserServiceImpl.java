package no.dnb.reskill.ec_webserver.services;
import no.dnb.reskill.ec_webserver.models.User;
import no.dnb.reskill.ec_webserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    public UserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }


    // Implementation of interface methods
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }


    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return null; //userRepository.findByUsername(username);
    }

    @Override
    public boolean updateDescriptionById(String id, String description) {
        return false;
    }
}

