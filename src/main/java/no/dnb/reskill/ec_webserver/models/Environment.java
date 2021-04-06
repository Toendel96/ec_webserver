package no.dnb.reskill.ec_webserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    @Getter private long id;
    private String short_name;
    private String description;

    @OneToMany(mappedBy = Configuration)


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
