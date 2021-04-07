package no.dnb.reskill.ec_webserver.repositories;

import no.dnb.reskill.ec_webserver.models.Environment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentRepository extends CrudRepository<Environment, Long> {

    //
    @Query("SELECT e FROM Environment e")
    Iterable<Environment> test(Long environment);
}
