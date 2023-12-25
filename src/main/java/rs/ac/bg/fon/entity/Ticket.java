package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;


/**
 * Class representing a ticket.
 *
 * Contains information about the wager amount, total odds of ticket,
 *      potential win, date and time of play, current state of ticket, bets that ticket contains and user that played the ticket.
 * Class attributes: id, wager, odd, totalWin, date, state, bets and user.
 *
 * @author Janko
 * @version 1.0
 */
@AllArgsConstructor
@Entity
public class Ticket {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "ticket_id")
    private Integer id;

    /**
     * Value of the amount wagered on ticket.
     */
    @Column(name = "wager")
    private BigDecimal wager;

    /**
     * Combined odds of all bets contained on ticket.
     */
    @Column(name = "total_odd")
    private double odd;

    /**
     * Total win if all bets pass.
     */
    @Column(name = "total_win")
    private BigDecimal totalWin;

    /**
     * The date and time when ticket is played.
     */
    @Column(name = "date")
    private LocalDateTime date;

    /**
     * Current state of ticket. Default value is "UNPROCESSED", value can be one of states defined in Constants class.
     */
    @Column(name = "state")
    private String state;

    /**
     * List of Bet objects that are contained in ticket.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "ticket")
    private List<Bet> bets;


    /**
     * User that has played the ticket.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    /**
     * Class constructor, sets attributes to their default values.
     */
    public Ticket() {
    }

    /**
     * Returns unique id of ticket.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets ticket id to value that is provided.
     *
     * @param id new Integer value for ticket id.
     * @throws NullPointerException if provided ID is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Ticket ID can not be null!");
        this.id = id;
    }

    /**
     * Returns the amount wagered on ticket.
     *
     * @return wager as a BigDecimal value.
     */
    public BigDecimal getWager() {
        return wager;
    }

    /**
     * Sets wager to the object that is provided.
     *
     * @param wager new object of class BigDecimal.
     * @throws NullPointerException if provided wager is null.
     */
    public void setWager(BigDecimal wager) {
        if (wager == null)
            throw new NullPointerException("Wager value can not be null!");
        this.wager = wager;
    }

    /**
     * Returns the value of ticket odds.
     *
     * @return odd as a double value.
     */
    public double getOdd() {
        return odd;
    }

    /**
     * Sets odd to the value that is provided.
     *
     * @param odd new double value for ticket odd.
     */
    public void setOdd(double odd) {
        this.odd = odd;
    }

    /**
     * Returns the potential win of the ticket.
     *
     * @return totalWin as a BigDecimal value.
     */
    public BigDecimal getTotalWin() {
        return totalWin;
    }
    /**
     * Sets totalWin to the object that is provided.
     *
     * @param totalWin new object of class BigDecimal.
     * @throws NullPointerException if provided totalWin is null.
     */
    public void setTotalWin(BigDecimal totalWin) {
        if (totalWin == null)
            throw new NullPointerException("Total win value can not be null!");
        this.totalWin = totalWin;
    }

    /**
     * Returns the date and time when ticket is played.
     *
     * @return date as a LocalDateTime value.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date and time, of then the ticket is played, to value that is provided.
     *
     * @param date new LocalDateTime value when the ticket is played.
     * @throws NullPointerException if provided date is null.
     */
    public void setDate(LocalDateTime date) {
        if (date == null)
            throw new NullPointerException("Ticket date can not be null!");
        this.date = date;
    }

    /**
     * Returns the current state of the ticket.
     *
     * @return state as a String value.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state to value that is provided.
     *
     * @param state new String value for ticket state.
     * @throws NullPointerException if provided state is null.
     */
    public void setState(String state) {
        if (state == null)
            throw new NullPointerException("Ticket state can not be null!");
        this.state = state;
    }

    /**
     * Returns bets that are contained in ticket.
     *
     * @return bets as a list of Bet objects contained in ticket,
     *          if bets attribute is null, then an empty Array List is returned.
     */
    public List<Bet> getBets() {
        if (this.bets == null) {
            bets = new ArrayList<>();
        }
        return bets;
    }

    /**
     * Sets bets to value that is provided.
     *
     * @param bets new List of Bet objects that are contained in ticket,
     *             if bets is null, an empty Array List is assigned instead
     */
    public void setBets(List<Bet> bets) {
        this.bets = Objects.requireNonNullElseGet(bets, ArrayList::new);
    }

    /**
     * Returns user that played the ticket.
     *
     * @return user as an Object of class User.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user who played the ticket to value that is provided.
     *
     * @param user new object of class User.
     * @throws NullPointerException if provided user is null.
     */
    public void setUser(User user) {
        if (user == null)
            throw new NullPointerException("Ticket user can not be null!");
        this.user = user;
    }
}
