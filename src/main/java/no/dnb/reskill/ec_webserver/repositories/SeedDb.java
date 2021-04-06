package no.dnb.reskill.ec_webserver.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SeedDb {

    @Autowired
    public SeedDb(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("We must add JPQL here to seed database");

        /*
//        EXAMPLES:
        jdbcTemplate.update("INSERT INTO Environment (id, street, house_number, house_section, post_code) VALUES (?,?,?,?,?)",
                new Object[]{ x, x, x, x, x });

                jdbcTemplate.update("INSERT INTO Configuration ( key_name, location_id) VALUES (?,?)",
                new Object[]{ "Hovedmåler", 1L });
         */


    }
}
