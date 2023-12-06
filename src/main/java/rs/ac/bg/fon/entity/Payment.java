package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Class representing payments.
 *
 * Contains information about the payment amount, type of payment and the user that is bound to payment
 * .
 * Class attributes: unique id, amount, payment type and user.
 *
 * @author Janko
 * @version 1.0
 */
@AllArgsConstructor
@Entity
public class Payment {

    /**
     * Unique id number representing the primary key in database table. ID is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id")
    private Integer id;

    /**
     * Payment amount.
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * Type of payment, value can be one of payment types defined in Constants class.
     */
    @Column(name = "payment_type")
    private String paymentType;

    /**
     * User that payment is bound to.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Class constructor, sets attributes to their default values".
     */
    public Payment() {
    }

    /**
     * Returns unique id of payment.
     *
     * @return id as an Integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets payment id to value that is provided.
     *
     * @param id new Integer value for payment id.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Payment ID can not be null!");
        this.id = id;
    }

    /**
     * Returns the payment amount.
     *
     * @return amount as an BigDecimal value.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets payment amount to value that is provided.
     *
     * @param amount new BigDecimal value for payment amount.
     * @throws NullPointerException if provided amount is null.
     */
    public void setAmount(BigDecimal amount) {
        if (amount == null)
            throw new NullPointerException("Payment amount can not be null!");
        this.amount = amount;
    }

    /**
     * Returns the type of payment.
     *
     * @return paymentType as an String value.
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * Sets payment type to value that is provided.
     *
     * @param paymentType new String value for payment type, defined in Constants class.
     * @throws NullPointerException if provided amount is null.
     */
    public void setPaymentType(String paymentType) {
        if (paymentType == null)
            throw new NullPointerException("Payment type can not be null!");
        this.paymentType = paymentType;
    }

    /**
     * Returns User object that payment is bound to.
     *
     * @return user as an Object of class User.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user to whom payment is bound to value that is provided.
     *
     * @param user new object of clas User.
     * @throws NullPointerException if provided user is null.
     */
    public void setUser(User user) {
        if (user == null)
            throw new NullPointerException("User for payment can not be null!");
        this.user = user;
    }
}
