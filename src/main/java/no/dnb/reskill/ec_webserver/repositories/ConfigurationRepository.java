package no.dnb.reskill.ec_webserver.repositories;

import no.dnb.reskill.ec_webserver.models.Configuration;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDateTime;

@EnableScan
public interface ConfigurationRepository extends CrudRepository<Configuration, String> {


}
