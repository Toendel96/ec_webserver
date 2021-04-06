package no.dnb.reskill.ec_webserver.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Order(3)
public class SeedDbConfiguration {

    @Autowired
    public SeedDbConfiguration(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update("INSERT INTO Configuration (environment_id, key_name, value ) VALUES (?,?,?)",
                new Object[]{ 1L, "Connection string for a database for application 1","" });
        jdbcTemplate.update("INSERT INTO Configuration ( environment_id, key_name, value ) VALUES (?,?,?)",
                new Object[]{ 1L, "Connection string for a database for application 2", 1L });
        jdbcTemplate.update("INSERT INTO Configuration ( environment_id, key_name, value ) VALUES (?,?,?)",
                new Object[]{ 1L, "•\tName of storage area in the cloud for application 4", 1L });
        jdbcTemplate.update("INSERT INTO Configuration ( environment_id, key_name, value ) VALUES (?,?,?)",
                new Object[]{ 2L, "Connection string for a database for application 3", 1L });
        jdbcTemplate.update("INSERT INTO Configuration ( environment_id, key_name, value ) VALUES (?,?,?)",
                new Object[]{ 3L, "•\tName of a message queue for application 4", 1L });

    }
}
