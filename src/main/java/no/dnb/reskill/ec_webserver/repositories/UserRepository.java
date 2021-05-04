package no.dnb.reskill.ec_webserver.repositories;
import no.dnb.reskill.ec_webserver.models.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface UserRepository extends CrudRepository <User,Long>{

//    @Query("SELECT u FROM User u WHERE u.username = ?1")
//    User findByUsername(String username);
}
