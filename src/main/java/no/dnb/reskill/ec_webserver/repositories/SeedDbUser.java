package no.dnb.reskill.ec_webserver.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;



@Component
@Order(1)
public class SeedDbUser {
    @Autowired
    public SeedDbUser(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);




    }
}
