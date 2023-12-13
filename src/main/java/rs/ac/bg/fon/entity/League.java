package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.management.InvalidAttributeValueException;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a Football League.
 *
 * Contains information about the name of the League and all fixtures that it has.
 * Class attributes: id, name and fixtures.
 *
 * @author Janko
 * @version 1.0
 */
@Data
@Entity
@Table(name = "league")
@AllArgsConstructor
public class League {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @Column(name = "league_id")
    private Integer id;

    /**
     * Name of the league.
     */
    @Column(name = "league_name")
    private String name;

    /**
     * List of Fixture objects that are contained in this league
     */
    @JsonIgnore
    @OneToMany(mappedBy = "league")
    private List<Fixture> fixtures;


    /**
     * Class constructor, sets attributes to their default values.
     */
    public League() {
    }

    /**
     * Returns a string representation of a league.
     *
     * @return A string representation of the object, either the 'name' attribute (e.g. Premier League)
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
     * Returns unique id of league.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets league id to value that is provided.
     *
     * @param id new Integer value for league id.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("League ID can not be null!");
        this.id = id;
    }


    /**
     * Returns the name of the league.
     *
     * @return name as a String value.
     */
    public String getName() {
        return name;
    }


    /**
     * Sets name to value that is provided.
     *
     * @param name new String value for league name.
     * @throws NullPointerException if provided name is null.
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("League name can not be null!");

        this.name = name;
    }

    /**
     * Returns fixtures that are contained in league.
     *
     * @return fixtures as a list of Fixture objects associated with league,
     *         if fixtures attribute is null, then an empty Array List is returned.
     */
    public List<Fixture> getFixtures() {
        if (fixtures == null) {
            fixtures = new ArrayList<>();
        }
        return fixtures;
    }

    /**
     * Sets list of Fixture objects to value that is provided.
     *
     * @param fixtures new List of Fixture objects for fixtures contained in league,
     *             if fixtures is null, an empty Array List is assigned instead
     */
    public void setFixtures(List<Fixture> fixtures) {
        this.fixtures = Objects.requireNonNullElseGet(fixtures, ArrayList::new);
    }
}
