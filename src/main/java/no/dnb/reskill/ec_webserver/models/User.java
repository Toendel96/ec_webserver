package no.dnb.reskill.ec_webserver.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String user_type;
    @Transient // Prevents the variable from being stored in database
    private String token;


    @OneToMany (mappedBy = "user")
    @JsonManagedReference
    private List<Configuration> configurations;


    /*
     id
- user (FK User.id) (hardcoded)
- password (hardcoded)
     */


}
