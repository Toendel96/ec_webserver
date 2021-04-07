package no.dnb.reskill.ec_webserver.repositories;
import no.dnb.reskill.ec_webserver.models.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository <User,Long>{
}
