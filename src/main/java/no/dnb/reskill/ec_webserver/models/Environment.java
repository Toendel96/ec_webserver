package no.dnb.reskill.ec_webserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="Environment")
public class Environment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) //This equals auto_increment
    private long id;
    private String short_name;
    private String description;

    //Kan være at Configuration skal være med liten c
    @OneToMany(mappedBy = "environment")
    @JsonBackReference
    private List<Configuration> configurations;



    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Environment) {
            Environment e = (Environment) other;
            result = this.id == e.id;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }
}
