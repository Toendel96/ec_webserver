package no.dnb.reskill.ec_webserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Configuration")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String key_name;
    private String value;
    private LocalDateTime timestamp_added;
    private LocalDateTime timestamp_modified;

    @ManyToOne // Other side must have @OneToMany(mappedBy = "configuration")
    @JsonBackReference // Other side must have @JsonManagedReference
    @JoinColumn(name="environment_id", nullable = false) // Not needed by other side
    private Environment environment; // To be replaced by Environment (from Petter)


    @ManyToOne // Other side must have @OneToMany(mappedBy = "configuration")
    @JsonBackReference // Other side must have @JsonManagedReference
    @JoinColumn(name="user_id", nullable = false) // Not needed by other side
    private User user; // To be replaced by User (from Salim)


    /*
     id
- environment (FK Environment.id)
- key_name
- value
- timestamp (added)
- timestamp (updated)
- modified_by (FK User.ID)
     */


}
