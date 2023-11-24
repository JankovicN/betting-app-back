package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.bg.fon.constants.Constants;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name = "bet")
public class Bet {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bet_id")
    private Integer id;

    /**
     * State of Bet. Default value is '-', value can be one of states defined in Constants class.
     */
    @Column(name = "state")
    private String state = Constants.BET_NOT_FINISHED;

    /**
     * Odd that Bet is placed on - contains information about Fixture that bet is placed on, value and name of Bet.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "odds_id", nullable = false)
    private Odd odd;

    /**
     *
     * Ticket that contains Bet.
     *
     */
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Bet() {
    }

    /**
     * Returns unique ID of Bet.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets Bet ID to value that is provided.
     *
     * @param id new Integer value for Bet ID.
     * @throws NullPointerException if provided ID is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Id can not be null!");

        this.id = id;
    }

    /**
     * Returns the current State of the Bet.
     *
     * @return State as a String value.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state to value that is provided.
     *
     * @param state new String value for Bet State.
     * @throws NullPointerException if provided State is null.
     */
    public void setState(String state) {
        if (state == null)
            throw new NullPointerException("State can not be null!");

        this.state = state;
    }

    /**
     * Returns Odd object which Bet is placed on.
     *
     * @return odd as an Object of class Odd.
     */
    public Odd getOdd() {
        return odd;
    }


    /**
     * Sets Odd to the object that is provided.
     *
     * @param odd new object of class Odd.
     * @throws NullPointerException if provided odd is null.
     */
    public void setOdd(Odd odd) {
        if (odd == null)
            throw new NullPointerException("Odd can not be null!");
        this.odd = odd;
    }

    /**
     * Returns the Ticket which contains this bet.
     *
     * @return ticket as an object of class Ticket.
     */
    public Ticket getTicket() {
        return ticket;
    }


    /**
     * Sets Ticket to the object that is provided.
     *
     * @param ticket new object of class Ticket.
     * @throws NullPointerException if provided ticket is null.
     */
    public void setTicket(Ticket ticket) {
        if (ticket == null)
            throw new NullPointerException("Ticket can not be null!");
        this.ticket = ticket;
    }
}
