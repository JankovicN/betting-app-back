package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Football League.
 *
 * Contains information about the name of the League and all fixtures that it has.
 * Class attributes: unique id, name, list of fixtures.
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
     * Name of the League.
     */
    @Column(name = "league_name")
    private String name;

    /**
     * List of Fixture objects that are contained in this League
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
     * Returns a string representation of a League.
     *
     * @return A string representation of the object, either the 'name' attribute
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
     * Returns unique ID of League.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets League ID to value that is provided.
     *
     * @param id new Integer value for League ID.
     * @throws NullPointerException if provided ID is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Id can not be null!");
        this.id = id;
    }


    /**
     * Returns the name of the League.
     *
     * @return name as a String value.
     */
    public String getName() {
        return name;
    }


    /**
     * Sets name to value that is provided.
     *
     * @param name new String value for League name.
     * @throws NullPointerException if provided name is null.
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Name can not be null!");

        this.name = name;
    }

    /**
     * Returns the list of Fixture objects that this League contains.
     *
     * @return fixtures as a list of Fixture objects associated with League,
     *          if fixtures attribute is null then an empty Array List is returned.
     */
    public List<Fixture> getFixtures() {
        if (fixtures == null) {
            fixtures = new ArrayList<>();
        }
        return fixtures;
    }


    /**
     * Sets list of Fixture to value that is provided.
     *
     * @param fixtures new List of Fixture objects for fixtures contained in League,
     *             if fixtures is null an empty Array List is assigned instead
     */
    public void setFixtures(List<Fixture> fixtures) {
        if (fixtures == null)
            this.fixtures = new ArrayList<>();
        else
        this.fixtures = fixtures;
    }
}
