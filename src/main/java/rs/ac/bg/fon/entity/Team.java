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
import java.util.Objects;

/**
 * Class representing a football Team.
 *
 * Contains information about the name of the team and fixtures where team is home side and fixtures where team is  away side.
 * Class attributes: id, name, home and away.
 *
 * @author Janko
 * @version 1.0
 */
@AllArgsConstructor
@Entity
public class Team {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @Column(name = "team_id")
    private Integer id;

    /**
     * Name of the team.
     */
    @Column(name = "team_name")
    private String name;

    /**
     * List of Fixture objects where team plays as home side
     */
    @JsonIgnore
    @OneToMany(mappedBy = "home", fetch = FetchType.EAGER)
    private List<Fixture> home;

    /**
     * List of Fixture objects where team plays as awayside
     */
    @JsonIgnore
    @OneToMany(mappedBy = "away", fetch = FetchType.EAGER)
    private List<Fixture> away;

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Team() {
    }

    /**
     * Returns a string representation of a team.
     *
     * @return A string representation of the object, either the 'name' attribute (e.g. Barcelona)
     *  or the result of super.toString() method
     */
    @Override
    public String toString() {
        if (StringUtils.isBlank(name)) {
            return super.toString();
        }
        return name;
    }

    /**
     * Returns unique id of team.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets team id to value that is provided.
     *
     * @param id new Integer value for team.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Team ID can not be null!");
        this.id = id;
    }

    /**
     * Returns the name of the team.
     *
     * @return name as a String value.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name to value that is provided.
     *
     * @param name new String value for team name.
     * @throws NullPointerException if provided name is null.
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Team name can not be null!");
        this.name = name;
    }

    /**
     * Returns fixtures in which team plays as home side.
     *
     * @return fixtures as a list of Fixture objects,
     *         if fixtures attribute is null, then an empty Array List is returned.
     */
    public List<Fixture> getHome() {
        if (this.home == null) {
            this.home = new ArrayList<>();
        }
        return home;
    }

    /**
     * Sets home side fixtures to value that is provided.
     *
     * @param home new List of Fixture objects for fixtures where team is home side,
     *             if fixtures is null, an empty Array List is assigned instead
     */
    public void setHome(List<Fixture> home) {
        this.home = Objects.requireNonNullElseGet(home, ArrayList::new);
    }

    /**
     * Returns fixtures in which team plays as away side.
     *
     * @return fixtures as a list of Fixture objects,
     *         if fixtures attribute is null, then an empty Array List is returned.
     */
    public List<Fixture> getAway() {
        if (this.away == null) {
            this.away = new ArrayList<>();
        }
        return away;
    }

    /**
     * Sets away side fixtures to value that is provided.
     *
     * @param away new List of Fixture objects for fixtures where team is away side,
     *             if fixtures is null, an empty Array List is assigned instead
     */
    public void setAway(List<Fixture> away) {
        this.away = Objects.requireNonNullElseGet(away, ArrayList::new);
    }




}
