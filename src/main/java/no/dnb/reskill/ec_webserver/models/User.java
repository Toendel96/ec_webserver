package no.dnb.reskill.ec_webserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    private String user_id;
    private String password;


    //@ManyToOne // Other side must have @OneToMany(mappedBy = "configuration")
    @JsonBackReference // Other side must have @JsonManagedReference
    @JoinColumn(name="environment_id", nullable = false)
    private Object environment; // To be replaced by Environment (from Petter)


    //@ManyToOne // Other side must have @OneToMany(mappedBy = "configuration")
    @JsonBackReference // Other side must have @JsonManagedReference
    @JoinColumn(name="user_id", nullable = false)
    private long user; // To be replaced by User (from Salim)


    /*
     id
- user (FK User.id) (hardcoded)
- password (hardcoded)
     */


}
