package no.dnb.reskill.ec_webserver.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SeedDbConfiguration {

    @Autowired
    public SeedDbConfiguration(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // ______________________
        jdbcTemplate.update("INSERT INTO User (id, username, password, user_type) VALUES (?,?,?,?)",
                new Object[]{ 1L, "Salim","pass1", "Admin"});
        jdbcTemplate.update("INSERT INTO User (id, username, password, user_type) VALUES (?,?,?,?)",
                new Object[]{ 2L, "Petter","pass2", "Admin"});
        jdbcTemplate.update("INSERT INTO User (id, username, password, user_type) VALUES (?,?,?,?)",
                new Object[]{ 3L, "Sigbjørn","pass3", "Admin"});

        // ______________________
        jdbcTemplate.update("INSERT INTO Environment (id, short_name, description) VALUES (?,?,?)",
                new Object[]{1L, "DEV", "Development environment"});
        jdbcTemplate.update("INSERT INTO Environment (id, short_name, description) VALUES (?,?,?)",
                new Object[]{2L, "PROD", "Production environment"});
        jdbcTemplate.update("INSERT INTO Environment (id, short_name, description) VALUES (?,?,?)",
                new Object[]{3L, "Test", "Testing environment"});
        jdbcTemplate.update("INSERT INTO Environment (id, short_name, description) VALUES (?,?,?)",
                new Object[]{4L, "DISREC", "Disaster recovery"});


        // _______________
        jdbcTemplate.update("INSERT INTO Configuration (environment_id, key_name, value, user_id ) VALUES (?,?,?,?)",
                new Object[]{ 1L,"Dev configuration 1","Dev value 1",1L});
        jdbcTemplate.update("INSERT INTO Configuration (environment_id, key_name, value, user_id ) VALUES (?,?,?,?)",
                new Object[]{ 2L,"Prod configuration 1","Prod value 1",2L});
        jdbcTemplate.update("INSERT INTO Configuration (environment_id, key_name, value, user_id ) VALUES (?,?,?,?)",
                new Object[]{ 2L,"Prod configuration 2","Prod value 2",2L});
        jdbcTemplate.update("INSERT INTO Configuration (environment_id, key_name, value, user_id ) VALUES (?,?,?,?)",
                new Object[]{ 3L,"Test configuration 1", "Test value 1",3L});
        jdbcTemplate.update("INSERT INTO Configuration (environment_id, key_name, value, user_id ) VALUES (?,?,?,?)",
                new Object[]{ 3L,"Test configuration 2", "Test value 2",3L});
        jdbcTemplate.update("INSERT INTO Configuration (environment_id, key_name, value, user_id ) VALUES (?,?,?,?)",
                new Object[]{ 3L,"Test configuration 3","Test value 3", 3L});

    }
}
