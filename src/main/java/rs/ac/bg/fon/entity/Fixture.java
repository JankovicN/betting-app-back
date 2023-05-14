package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="fixture")
public class Fixture {

    @Id
    @Column(name="fixture_id")
    private Integer id;
    @Column(name="date")
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="home_id")
    private Team home;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="away_id")
    private Team away;

    @Column(name="home_goals")
    private int homeGoals;
    @Column(name="away_goals")
    private int awayGoals;
    @Column(name="state")
    private String state="NS";

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="league_id", nullable=false)
    private League league;

    @JsonIgnore
    @OneToMany(mappedBy = "fixture")
    private List<Odd> odds;

    @Transient
    private List<BetGroup> betGroupList;
}
