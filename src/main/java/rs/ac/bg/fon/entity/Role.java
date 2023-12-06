package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

/**
 * Class representing user Role.
 *
 * Contains information about the name of the role.
 * Class attributes: id and name.
 *
 * @author Janko
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
public class Role {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "role_id")
    private Integer id;

    /**
     * Name of the role.
     */
    @Column(name = "role_name")
    private String name;

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Role() {
    }

    /**
     * Returns unique id of role.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets role id to value that is provided.
     *
     * @param id new Integer value for role id.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Role ID can not be null!");
        this.id = id;
    }

    /**
     * Returns the name of the role.
     *
     * @return name as a String value.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name to value that is provided.
     *
     * @param name new String value for role name.
     * @throws NullPointerException if provided name is null.
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Role name can not be null!");
        this.name = name;
    }
}
