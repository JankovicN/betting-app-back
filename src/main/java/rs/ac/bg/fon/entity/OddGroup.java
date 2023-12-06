package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing odd groups, each group is associated with a list of odds.
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
     * Name of the odd group.
     */
    @Column(name = "odd_group_name")
    private String name;

    /**
     * List of Odd objects that are contained in this odd group
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "oddGroup", fetch = FetchType.LAZY)
    private List<Odd> odds;



    /**
     * Returns a string representation of the odd group.
     *
     * @return A string representation of the object, either the 'name' attribute (e.g. Match Winner)
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
     * Returns unique ID of odd group.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets odd group id to value that is provided.
     *
     * @param id new Integer value  for odd group id.
     * @throws NullPointerException if provided ID is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Odd Group ID can not be null!");

        this.id = id;

    }

    /**
     * Returns the name of odd group.
     *
     * @return name as a String value.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name to value that is provided.
     *
     * @param name new String value for odd group name.
     * @throws NullPointerException if provided name is null.
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Name can not be null!");

        this.name = name;
    }

    /**
     * Sets odds to value that is provided.
     *
     * @param odds new List of Odd objects for odds contained in odd group,
     *             if odds is null, an empty Array List is assigned instead
     */
    public void setOdds(List<Odd> odds) {
        this.odds = Objects.requireNonNullElseGet(odds, ArrayList::new);
    }

    /**
     * Returns list of Odd objects that odd group contains.
     *
     * @return odds as a list of Odd objects associated with odd group,
     *          if odds attribute is null then an empty Array List is returned.
     */
    public List<Odd> getOdds() {
        if (odds == null) {
            odds = new ArrayList<>();
        }
        return odds;
    }
}
