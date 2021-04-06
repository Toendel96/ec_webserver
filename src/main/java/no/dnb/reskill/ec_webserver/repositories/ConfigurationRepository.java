package no.dnb.reskill.ec_webserver.repositories;

import no.dnb.reskill.ec_webserver.models.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {
    // Nothing more needed here unless special functionality is needed.

    @Query("SELECT c FROM Configuration c WHERE c.environment.id = ?1")
    Iterable<Configuration> findByEnvironmentId(Long environmentId);
}
