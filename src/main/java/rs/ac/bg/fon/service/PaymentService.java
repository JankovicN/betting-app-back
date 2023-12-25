package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Payment.PaymentDTO;
import rs.ac.bg.fon.entity.Payment;
import rs.ac.bg.fon.utility.ApiResponse;

import java.math.BigDecimal;

/**
 * Represents a service layer interface responsible for defining all Payment related methods.
 * Available API method implementations: GET, POST
 *
 * @author Janko
 * @version 1.0
 */
@Service
public interface PaymentService {

    /**
     * Returns the sum of all user payments, in other words his balance.
     *
     * @param userId Integer value representing id of user for which we are fetching balance.
     * @return BigDecimal value representing user balance,
     * or BigDecimal of zero if error occurs.
     */
    BigDecimal getUserPayments(Integer userId);

    /**
     * Adds new payment to database, if all checks pass.
     *
     * @param payment instance of Payment class, containing information about the payment amount, type and the user who is making it.
     * @return instance of Payment class representing the payment that is added,
     * or null if any error occurs.
     */
    Payment addPayment(Payment payment);

    /**
     * Creates and adds new payment to database, if all checks pass.
     *
     * @param userId Integer value representing id of user who is making the payment
     * @param amount BigDecimal value representing the payment amount.
     * @param type   String value representing the type of the payment.
     * @return instance of Payment class representing the payment that is added,
     * or null if any error occurs.
     */
    Payment addPayment(Integer userId, BigDecimal amount, String type);

    /**
     * Checks if user can pay the specified amount based on his balance.
     *
     * @param userId Integer value representing id of user for which we are checking.
     * @param amount BigDecimal value for the amount we are trying to add/subtract to/from user balance.
     * @return boolean value, return true if user can pay the specified amount,
     * otherwise return false.
     */
    boolean canUserPay(Integer userId, BigDecimal amount);

    /**
     * Returns response for API call, containing user balance.
     *
     * @param userId Integer value representing id of user for which we are fetching balance.
     * @return instance of ApiResponse class, containing balance for specified user.
     */
    ApiResponse<?> getUserPaymentsApiResponse(Integer userId);

    /**
     * Returns response for API call, containing information about the success of adding user payment.
     *
     * @param payment instance of PaymentDTO class, containing information about the user and the amount of the payment.
     * @param type    String value representing the type of the payment.
     * @return instance of ApiResponse class, containing messages regarding the success of the operation.
     */
    ApiResponse<?> addPaymentApiResponse(PaymentDTO payment, String type);

}
