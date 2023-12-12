package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.dtos.Payment.PaymentDTO;
import rs.ac.bg.fon.entity.Payment;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.repository.PaymentRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import java.math.BigDecimal;

/**
 * Represents a service layer class responsible for implementing all Payment related methods.
 * Available API method implementations: GET, POST
 *
 * @author Janko
 * @version 1.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    /**
     * Instance of Logger class, responsible for displaying messages that contain information about the success of methods inside Payment service class.
     */
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    /**
     * Instance of Payment repository class, responsible for interacting with payment table in database.
     */
    private final PaymentRepository paymentRepository;

    /**
     * Checks if user can pay the specified amount based on his balance.
     *
     * @param userId Integer value representing id of user for which we are checking.
     * @param amount BigDecimal value for the amount we are trying to add/subtract to/from user balance.
     * @return boolean value, return true if user can pay the specified amount,
     *         otherwise return false.
     *
     */
    @Transactional
    @Override
    public boolean canUserPay(Integer userId, BigDecimal amount) {
        try {
            if (amount.compareTo(BigDecimal.ZERO) >= 0) {
                return true;
            }
            return getUserPayments(userId).add(amount).compareTo(BigDecimal.ZERO) >= 0;
        } catch (Exception e) {
            logger.error("Error while trying check if user can pay!", e);
            return false;
        }
    }

    /**
     * Returns the sum of all user payments, in other words his balance.
     *
     * @param userId Integer value representing id of user for which we are fetching balance.
     * @return BigDecimal value representing user balance,
     *         or BigDecimal.ZERO (0) if error occurs.
     *
     */
    @Transactional
    @Override
    public BigDecimal getUserPayments(Integer userId) {
        try {
            Double userPayments = paymentRepository.getUserPayments(userId);
            if (userPayments == null || userPayments.isNaN()) {
                logger.error("Invalid balance returned!");
                return BigDecimal.ZERO;
            }
            return BigDecimal.valueOf(userPayments);
        } catch (Exception e) {
            logger.error("Error while trying get User balance!", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Adds new payment to database, if all checks pass.
     *
     * @param payment instance of Payment class, containing information about the payment amount, type and the user who is making it.
     *
     */
    @Transactional
    @Override
    public void addPayment(Payment payment) {
        try {
            if (payment == null
                    || payment.getAmount() == null
                    || payment.getUser() == null) {
                logger.error("Error while trying add Payment, invalid data provided!");
            } else if (!canUserPay(payment.getUser().getId(), payment.getAmount())) {
                logger.error("Insufficient funds!");
            } else {
                paymentRepository.saveAndFlush(payment);
            }
        } catch (Exception e) {
            logger.error("Error while trying add Payment!", e);
        }

    }

    /**
     * Creates and adds new payment to database, if all checks pass.
     *
     * @param userId Integer value representing id of user who is making the payment
     * @param amount BigDecimal value representing the payment amount.
     * @param type String value representing the type of the payment.
     *
     */
    @Transactional
    @Override
    public void addPayment(Integer userId, BigDecimal amount, String type) {
        try {
            if (userId == null || amount == null || type == null) {
                logger.error("Error while trying add Payment, invalid data provided!");
            } else if (!canUserPay(userId, amount)) {
                logger.error("Insufficient funds!");
            } else {
                User user = new User();
                user.setId(userId);
                Payment payment = new Payment();
                payment.setAmount(amount);
                payment.setUser(user);
                payment.setPaymentType(type);
                paymentRepository.saveAndFlush(payment);
            }
        } catch (Exception e) {
            logger.error("Error while trying add Payment!", e);
        }
    }

    /**
     * Returns response for API call, containing user balance.
     *
     * @param userId Integer value representing id of user for which we are fetching balance.
     * @return instance of ApiResponse class, containing balance for specified user.
     *
     */
    @Override
    public ApiResponse<?> getUserPaymentsApiResponse(Integer userId) {
        return ApiResponseUtil.transformObjectToApiResponse(getUserPayments(userId), "balance");
    }

    /**
     * Returns response for API call, containing information about the success of adding user payment.
     *
     * @param payment instance of PaymentDTO class, containing information about the user and the amount of the payment.
     * @param type String value representing the type of the payment.
     * @return instance of ApiResponse class, containing messages regarding the success of the operation.
     *
     */
    @Override
    public ApiResponse<?> addPaymentApiResponse(PaymentDTO payment, String type) {
        ApiResponse<?> response = new ApiResponse<>();
        if (canUserPay(payment.getUserId(), payment.getAmount())) {
            addPayment(payment.getUserId(), payment.getAmount(), type);
            response.addInfoMessage("Successful transaction!");
        } else {
            response.addErrorMessage("Insufficient funds!");
        }
        return response;
    }
}
