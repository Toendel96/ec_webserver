package no.dnb.reskill.ec_webserver.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Order(2)
public class SeedDBEnvironment {
    @Autowired
    public SeedDBEnvironment(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update("INSERT INTO Environment (id, short_name, description) VALUES (?,?,?)",
                new Object[]{1L, "DEV", "Development environment"});
        jdbcTemplate.update("INSERT INTO Environment (id, short_name, description) VALUES (?,?,?)",
                new Object[]{2L, "PROD", "Production environment"});
        jdbcTemplate.update("INSERT INTO Environment (id, short_name, description) VALUES (?,?,?)",
                new Object[]{3L, "Test", "Testing environment"});
        jdbcTemplate.update("INSERT INTO Environment (id, short_name, description) VALUES (?,?,?)",
                new Object[]{4L, "DISREC", "Disaster recovery"});

    }
}
