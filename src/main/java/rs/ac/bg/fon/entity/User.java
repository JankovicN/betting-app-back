package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

/**
 * Class representing user.
 *
 * Contains information about the name, surname, email, username, password, birthday and roles that are assigned to user.
 * Class attributes: id, name, surname, email, username, password, birthday and roles.
 *
 * @author Janko
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
public class User {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "user_id")
    private Integer id;

    /**
     * Name of the user.
     */
    @Column(name = "name")
    private String name;

    /**
     * Surname of the user.
     */
    @Column(name = "surname")
    private String surname;

    /**
     * Email of the user.
     */
    @Column(name = "email")
    private String email;

    /**
     * The date user is born.
     */
    @Column(name = "birthday")
    private LocalDate birthday;

    /**
     * Username of the user.
     */
    @Column(name = "username")
    private String username;

    /**
     * Password of the user, used for authentication.
     */
    @Column(name = "password")
    private String password;


    /**
     * Collection of user roles. User can have one or more roles.
     */
    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles;

    @Override
    public String toString() {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(surname)) {
            return super.toString();
        }
        return StringUtils.capitalize(name) + " " + StringUtils.capitalize(surname);
    }

    /**
     * Class constructor, sets attributes to their default values and state to "NS".
     */
    public User() {
        roles = new ArrayList<>();
    }

    /**
     * Returns unique id of user.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets user id to value that is provided.
     *
     * @param id new Integer value for user id.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("User ID can not be null!");
        this.id = id;
    }

    /**
     * Returns the name of the user.
     *
     * @return name as a String value.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name to value that is provided.
     *
     * @param name new String value for the name of user.
     * @throws NullPointerException if provided name is null.
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Name of user can not be null!");
        this.name = name;
    }

    /**
     * Returns the surname of the user.
     *
     * @return surname as a String value.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets surname to value that is provided.
     *
     * @param surname new String value for the surname of user.
     * @throws NullPointerException if provided surname is null.
     */
    public void setSurname(String surname) {
        if (surname == null)
            throw new NullPointerException("Surname of user can not be null!");
        this.surname = surname;
    }

    /**
     * Returns the user email.
     *
     * @return email as a String value.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email to value that is provided.
     *
     * @param email new String value for user email.
     * @throws NullPointerException if provided email is null.
     */
    public void setEmail(String email) {
        if (email == null)
            throw new NullPointerException("Email can not be null!");
        this.email = email;
    }

    /**
     * Returns the birthday of user.
     *
     * @return birthday as a LocalDate value.
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Sets birthday to value that is provided.
     *
     * @param birthday new LocalDate value for user birthday.
     * @throws NullPointerException if provided birthday is null.
     */
    public void setBirthday(LocalDate birthday) {
        if (birthday == null)
            throw new NullPointerException("User birthday can not be null!");
        this.birthday = birthday;
    }

    /**
     * Returns username of user.
     *
     * @return username as a String value.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username to value that is provided.
     *
     * @param username new String value for username.
     * @throws NullPointerException if provided username is null.
     */
    public void setUsername(String username) {
        if (username == null)
            throw new NullPointerException("Username can not be null!");
        this.username = username;
    }

    /**
     * Returns password of user.
     *
     * @return password as a String value.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password to value that is provided.
     *
     * @param password new String value for password.
     * @throws NullPointerException if provided password is null.
     */
    public void setPassword(String password) {
        if (password == null)
            throw new NullPointerException("Password can not be null!");
        this.password = password;
    }

    /**
     * Returns all roles that are assigned to user.
     *
     * @return roles as a Collection of Role Objects.
     */
    public Collection<Role> getRoles() {
        if(roles==null){
            roles= new ArrayList<>();
        }
        return roles;
    }
    /**
     * Sets roles to value that is provided.
     *
     * @param roles new Collection of Role Objects to be assigned to user roles.
     * @throws NullPointerException if provided password is null.
     */
    public void setRoles(Collection<Role> roles) {
        if (roles == null)
            throw new NullPointerException("User roles can not be null!");
        this.roles = roles;
    }
}
