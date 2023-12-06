package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import rs.ac.bg.fon.constants.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a football Fixture.
 *
 * Contains information about the following: start date and time, home team, away team, home team goals, away team goals,
 *      state of the fixture (Not Started, Half Time, Full Time etc.), League in which the fixture is being played, odds that are associated with the fixture.
 * Class attributes: unique id, home team, away team, home team goals, away team goals, state, league and list of odds.
 *
 * @author Janko
 * @version 1.0
 */
@Entity
@AllArgsConstructor
@Table(name = "fixture")
public class Fixture {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @Column(name = "fixture_id")
    private Integer id;

    /**
     * The date and time when the fixture starts.
     */
    @Column(name = "date")
    private LocalDateTime date;

    /**
     * Team object that plays for the home side in fixture.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "home_id")
    private Team home;

    /**
     * Team object that plays for the away side in fixture.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "away_id")
    private Team away;

    /**
     * Number of goals that home team has scored.
     */
    @Column(name = "home_goals")
    private int homeGoals;

    /**
     * Number of goals that away team has scored.
     */
    @Column(name = "away_goals")
    private int awayGoals;


    /**
     * Current state of fixture. Default value is "NS", value can be one of states defined in Constants class.
     */
    @Column(name = "state")
    private String state = Constants.FIXTURE_NOT_STARTED;

    /**
     * League that fixture is being played in.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    /**
     * List of Odd objects that are associated with  fixture.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "fixture", fetch = FetchType.LAZY)
    private List<Odd> odds;

    /**
     * Returns a string representation of the fixture.
     *
     * @return A string representation of the object, either in format '[home team name] - [away team name]' (e.g. Arsenal - Brighton)
     *  or the result of super.toString() method
     */
    @Override
    public String toString() {
        if (home == null || away == null || StringUtils.isBlank(home.getName()) || StringUtils.isBlank(away.getName())) {
            return super.toString();
        }
        return StringUtils.capitalize(home.getName()) + " - " + StringUtils.capitalize(away.getName());
    }

    /**
     * Class constructor, sets attributes to their default values and state to "NS".
     */
    public Fixture() {
        setState(Constants.FIXTURE_NOT_STARTED);
    }

    /**
     * Returns unique id of fixture.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets fixture id to value that is provided.
     *
     * @param id new Integer value  for fixture id.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Fixture ID can not be null!");

        this.id = id;
    }

    /**
     * Returns the date and time of the fixture start.
     *
     * @return date as a LocalDateTime value.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets fixture start date and time to value that is provided.
     *
     * @param date new LocalDateTime value  for fixture start date and time.
     * @throws NullPointerException if provided date is null.
     */
    public void setDate(LocalDateTime date) {
        if (date == null)
            throw new NullPointerException("Fixture date can not be null!");

        this.date = date;
    }

    /**
     * Returns Team object that is the home side of fixture.
     *
     * @return home as an Object of class Team.
     */
    public Team getHome() {
        return home;
    }

    /**
     * Sets home to the object that is provided.
     *
     * @param home new object of class Team.
     * @throws NullPointerException if provided home is null.
     */
    public void setHome(Team home) {
        if (home == null)
            throw new NullPointerException("Home team can not be null!");
        this.home = home;
    }

    /**
     * Returns Team object that is the away side of fixture.
     *
     * @return away as an Object of class Team.
     */
    public Team getAway() {
        return away;
    }

    /**
     * Sets away to the object that is provided.
     *
     * @param away new object of class Team.
     * @throws NullPointerException if provided away is null.
     */
    public void setAway(Team away) {
        if (away == null)
            throw new NullPointerException("Away team can not be null!");
        this.away = away;
    }

    /**
     * Returns the number of goals that home side has scored.
     *
     * @return homeGoals as an int value.
     */
    public int getHomeGoals() {
        return homeGoals;
    }

    /**
     * Sets homeGoals to value that is provided.
     *
     * @param homeGoals new int value for home goals.
     *
     */
    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    /**
     * Returns the number of goals that away side has scored.
     *
     * @return awayGoals as an int value.
     */
    public int getAwayGoals() {
        return awayGoals;
    }

    /**
     * Sets awayGoals to value that is provided.
     *
     * @param awayGoals new int value for away goals.
     *
     */
    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    /**
     * Returns the current state of the fixture.
     *
     * @return state as a String value.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state to value that is provided.
     *
     * @param state new String value for fixture state.
     * @throws NullPointerException if provided state is null.
     */
    public void setState(String state) {
        if (state == null)
            throw new NullPointerException("Fixture state can not be null!");

        this.state = state;
    }

    /**
     * Returns League object which fixture is played in.
     *
     * @return league as an Object of class League.
     */
    public League getLeague() {
        return league;
    }

    /**
     * Sets league to the object that is provided.
     *
     * @param league new object of class League.
     * @throws NullPointerException if provided league is null.
     */
    public void setLeague(League league) {
        if (league == null)
            throw new NullPointerException("League can not be null!");
        this.league = league;
    }

    /**
     * Returns list of Odd objects that are associated with the fixture.
     *
     * @return odds as a list of Odd objects associated with the fixture,
     *          if odds attribute is null, then an empty Array List is returned.
     */
    public List<Odd> getOdds() {
        if (odds == null) {
            odds = new ArrayList<>();
        }
        return odds;
    }

    /**
     * Sets odds to value that is provided.
     *
     * @param odds new List of Odd objects for odds associated with the fixture,
     *             if odds is null, an empty Array List is assigned instead
     */
    public void setOdds(List<Odd> odds) {
        this.odds = Objects.requireNonNullElseGet(odds, ArrayList::new);
    }
}
