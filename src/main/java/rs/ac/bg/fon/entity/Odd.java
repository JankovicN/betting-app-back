package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing odd.
 *
 * Contains information about the value and name of odd, fixture that contains the odd, odd group which contains odd and list of bets that are placed on the odd.
 * Class attributes: unique id, odd value, name, fixture, odd group and list of bets.
 *
 * @author Janko
 * @version 1.0
 */
@Entity
@AllArgsConstructor
@Table(name = "odd")
public class Odd {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "odd_id")
    private Integer id;

    /**
     * Value of the odd.
     */
    @Column(name = "odd_value")
    private BigDecimal oddValue;

    /**
     * Name of the odd.
     */
    @Column(name = "odd_name")
    private String name;

    /**
     * Fixture object that contains the odd.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixture_id")
    private Fixture fixture;

    /**
     * OddGroup object that contains the odd.
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odd_group_id")
    private OddGroup oddGroup;

    /**
     * List of Bet objects that are placed on the odd.
     */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "odd")
    private List<Bet> bet;

    /**
     * Returns a string representation of the odd.
     *
     * @return A string representation of the object, either in format '[odd name] - [odd value]' (e.g. Home 1.23)
     *  or the result of super.toString() method
     */
    @Override
    public String toString() {
        if (StringUtils.isBlank(name)) {
            return super.toString();
        }
        return name + " " + oddValue;
    }

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Odd() {
    }

    /**
     * Returns unique id of odd.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets odd ID to value that is provided.
     *
     * @param id new Integer value for odd id.
     * @throws NullPointerException if provided ID is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Odd ID can not be null!");
        this.id = id;
    }

    /**
     * Returns the value of the odd.
     *
     * @return oddValue as a BigDecimal value.
     */
    public BigDecimal getOddValue() {
        return oddValue;
    }

    /**
     * Sets oddValue to the object that is provided.
     *
     * @param odd new object of class BigDecimal.
     * @throws NullPointerException if provided odd is null.
     */
    public void setOddValue(BigDecimal odd) {
        if (odd == null)
            throw new NullPointerException("Odd value can not be null!");
        this.oddValue = odd;
    }

    /**
     * Returns the name of the odd.
     *
     * @return name as a String value.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name to value that is provided.
     *
     * @param name new String value for odd name.
     * @throws NullPointerException if provided name is null.
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Odd name can not be null!");
        this.name = name;
    }

    /**
     * Returns Fixture object that contains odd.
     *
     * @return fixture as an Object of class Fixture.
     */
    public Fixture getFixture() {
        return fixture;
    }

    /**
     * Sets fixture to the object that is provided.
     *
     * @param fixture new object of class Fixture.
     * @throws NullPointerException if provided fixture is null.
     */
    public void setFixture(Fixture fixture) {
        if (fixture == null)
            throw new NullPointerException("Fixture can not be null!");
        this.fixture = fixture;
    }

    /**
     * Returns OddGroup object that contains odd.
     *
     * @return oddGroup as an Object of class OddGroup.
     */
    public OddGroup getOddGroup() {
        return oddGroup;
    }

    /**
     * Sets oddGroup to the object that is provided.
     *
     * @param oddGroup new object of class OddGroup.
     * @throws NullPointerException if provided oddGroup is null.
     */
    public void setOddGroup(OddGroup oddGroup) {
        if (oddGroup == null)
            throw new NullPointerException("OddGroup can not be null!");
        this.oddGroup = oddGroup;
    }

    /**
     * Returns list of Bet objects that are associated with the odd.
     *
     * @return bet as a list of Bet objects associated with the odd,
     *          if bet attribute is null, then an empty Array List is returned.
     */
    public List<Bet> getBet() {
        if (bet == null) {
            bet = new ArrayList<>();
        }
        return bet;
    }

    /**
     * Sets bet to value that is provided.
     *
     * @param bet new List of Bet objects for bet associated with the odd,
     *             if bet is null, an empty Array List is assigned instead
     */
    public void setBet(List<Bet> bet) {
        this.bet = Objects.requireNonNullElseGet(bet, ArrayList::new);
    }
}
