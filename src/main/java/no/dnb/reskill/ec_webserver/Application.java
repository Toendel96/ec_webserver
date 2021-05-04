package no.dnb.reskill.ec_webserver;


import no.dnb.reskill.ec_webserver.repositories.SeedDb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.*;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.validation.constraints.Null;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        try {
//            SeedDb seeding = new SeedDb();
//        }
//        catch (NullPointerException|InterruptedException e) {
//            System.out.println("Oh no, failure on seeding: \n " + e.getMessage());
//        }

    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("no.dnb.reskill.ec_webserver.controllers"))
                .paths(PathSelectors.ant("/**"))
                .build();
    }


//    @EnableWebSecurity
//    @Configuration
//    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.csrf().disable()
//                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//                    .authorizeRequests()
//                    .antMatchers(HttpMethod.POST, "/user").permitAll()
//                    .anyRequest().authenticated();
//        }
//    }

}
