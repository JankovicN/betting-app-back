package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Team {

    @Id
    @Column(name = "team_id")
    private Integer id;
    @Column(name = "team_name")
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "home", fetch = FetchType.EAGER)
    private List<Fixture> home;
    @JsonIgnore
    @OneToMany(mappedBy = "away", fetch = FetchType.EAGER)
    private List<Fixture> away;

    public List<Fixture> getHome() {
        if (this.home == null) {
            this.home = new ArrayList<>();
        }
        return home;
    }

    public List<Fixture> getAway() {
        if (this.away == null) {
            this.away = new ArrayList<>();
        }
        return away;
    }

    @Override
    public String toString() {
        if (StringUtils.isBlank(name)) {
            return super.toString();
        }
        return name;
    }
}
