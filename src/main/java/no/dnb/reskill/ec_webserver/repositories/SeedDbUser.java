package no.dnb.reskill.ec_webserver.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

public class SeedDbUser {

    @Component
    @Order(1)
    public class SeedDbConfiguration {

        @Autowired
        public SeedDbConfiguration(DataSource dataSource) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            jdbcTemplate.update("INSERT INTO User (id, username, password, user_type) VALUES (?,?,?,?)",
                    new Object[]{ 1L, "Salim","test1", "Admin"});


}
