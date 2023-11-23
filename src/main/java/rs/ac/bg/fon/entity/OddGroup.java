package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing Odd Groups, each group is associated with a list of odds.
 *
 * Contains information about the name of the group and all odds that are in the group.
 * Class attributes: unique id, name, list of odds.
 *
 * @author Janko
 * @version 1.0
 */
@AllArgsConstructor
@Entity
@Table(name = "odd_group")
public class OddGroup {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @Column(name = "odd_group_id")
    private Integer id;

    /**
     * Name of the Odd Group.
     */
    @Column(name = "odd_group_name")
    private String name;

    /**
     * List of Odd objects that are contained in this Odd Group
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "oddGroup", fetch = FetchType.LAZY)
    private List<Odd> odds;



    /**
     * Returns a string representation of the Odd Group.
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
     * Class constructor, sets attributes to their default values.
     */
    public OddGroup() {
    }

    /**
     * Returns unique ID of Odd Group.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets Odd Group ID to value that is provided.
     *
     * @param id new Integer value as ID.
     * @throws NullPointerException if provided ID is null.
     */
    public void setId(Integer id) {

        if (id == null)
            throw new NullPointerException("Id can not be null!");

        this.id = id;

    }

    /**
     * Returns the name of Odd Group.
     *
     * @return name as a String value.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name to value that is provided.
     *
     * @param name new String value for Odd Group name.
     * @throws NullPointerException if provided name is null.
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Name can not be null!");

        this.name = name;
    }

    /**
     * Sets list of Odds to value that is provided.
     *
     * @param odds new List of odds for odds contained in Odd Group,
     *             if odds is null an empty Array List is assigned instead
     */
    public void setOdds(List<Odd> odds) {
        if (odds == null)
            this.odds = new ArrayList<>();
        else
            this.odds = odds;
    }

    /**
     * Returns the list of Odd objects that Odd Group contains.
     *
     * @return odds as a list of Odd objects associated with Odd Group,
     *          if odds attribute is null then an empty Array List is returned
     */
    public List<Odd> getOdds() {
        if (odds == null) {
            odds = new ArrayList<>();
        }
        return odds;
    }
}
