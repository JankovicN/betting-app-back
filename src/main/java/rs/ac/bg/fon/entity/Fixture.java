package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import rs.ac.bg.fon.constants.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fixture")
public class Fixture {

    @Id
    @Column(name = "fixture_id")
    private Integer id;
    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "home_id")
    private Team home;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "away_id")
    private Team away;

    @Column(name = "home_goals")
    private int homeGoals;
    @Column(name = "away_goals")
    private int awayGoals;
    @Column(name = "state")
    private String state = Constants.FIXTURE_NOT_STARTED;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @JsonIgnore
    @OneToMany(mappedBy = "fixture",fetch = FetchType.LAZY)
    private List<Odd> odds;

    @Transient
    private List<BetGroup> betGroupList;

    @Override
    public String toString() {
        if (home == null || away == null || StringUtils.isBlank(home.getName()) || StringUtils.isBlank(away.getName())) {
            return super.toString();
        }
        return StringUtils.capitalize(home.getName()) + " - " + StringUtils.capitalize(away.getName());
    }
}
