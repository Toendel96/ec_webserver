package no.dnb.reskill.ec_webserver.repositories;
import no.dnb.reskill.ec_webserver.models.Configuration;
import no.dnb.reskill.ec_webserver.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository <User,Long>{

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);
}
