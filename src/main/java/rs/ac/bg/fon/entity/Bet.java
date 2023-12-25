package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import rs.ac.bg.fon.constants.Constants;

import javax.persistence.*;

/**
 * Class representing a single bet placed on  ticket.
 *
 * Contains information about the state of the bet, the odd associated with the bet and the ticket it is placed on.
 * Class attributes: id, state, odd and ticket.
 *
 * @author Janko
 * @version 1.0
 */
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
     * Current state of bet. Default value is '-', value can be one of states defined in Constants class.
     */
    @Column(name = "state")
    private String state = Constants.BET_NOT_FINISHED;

    /**
     * Odd that bet is placed on - contains information about Fixture that bet is placed on, value and name of bet.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "odd_id", nullable = false)
    private Odd odd;

    /**
     *
     * Ticket that contains bet.
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
     * Returns unique id of bet.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets bet id to value that is provided.
     *
     * @param id new Integer value for bet id.
     * @throws NullPointerException if provided ID is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Bet ID can not be null!");

        this.id = id;
    }

    /**
     * Returns the current state of the bet.
     *
     * @return state as a String value.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state to value that is provided.
     *
     * @param state new String value for bet state.
     * @throws NullPointerException if provided state is null.
     */
    public void setState(String state) {
        if (state == null)
            throw new NullPointerException("Bet state can not be null!");

        this.state = state;
    }

    /**
     * Returns odd which bet is placed on.
     *
     * @return odd as an Object of class Odd.
     */
    public Odd getOdd() {
        return odd;
    }

    /**
     * Sets odd to the object that is provided.
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
     * Returns the ticket which contains this bet.
     *
     * @return ticket as an object of class Ticket.
     */
    public Ticket getTicket() {
        return ticket;
    }


    /**
     * Sets ticket to the object that is provided.
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
