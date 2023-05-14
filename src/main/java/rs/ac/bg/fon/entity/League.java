package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="league")
public class League {

    @Id
    @Column(name="league_id")
    private Integer id;
    @Column(name="league_name")
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy="league")
    private List<Fixture> fixtures;

}
