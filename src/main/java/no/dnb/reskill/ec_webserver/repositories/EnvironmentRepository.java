package no.dnb.reskill.ec_webserver.repositories;

import no.dnb.reskill.ec_webserver.models.Environment;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@Repository
@EnableScan
public interface EnvironmentRepository extends CrudRepository<Environment, Long> {

    //Not used
//    @Query("SELECT e FROM Environment e")
//    Iterable<Environment> test(Long environment);
}
