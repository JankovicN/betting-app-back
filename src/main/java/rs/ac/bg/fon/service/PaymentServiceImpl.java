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

    @Transactional
    @Override
    public Payment addPayment(Payment payment) {
        try {
            if (payment == null
                    || payment.getAmount() == null
                    || payment.getUser() == null) {
                logger.error("Error while trying add Payment, invalid data provided!");
                return null;
            } else if (!canUserPay(payment.getUser().getId(), payment.getAmount())) {
                logger.error("Insufficient funds!");
                return null;
            } else {
                return paymentRepository.saveAndFlush(payment);
            }
        } catch (Exception e) {
            logger.error("Error while trying add Payment!", e);
            return null;
        }
    }

    @Transactional
    @Override
    public Payment addPayment(Integer userId, BigDecimal amount, String type) {
        try {
            if (userId == null || amount == null || type == null) {
                logger.error("Error while trying add Payment, invalid data provided!");
                return null;
            } else if (!canUserPay(userId, amount)) {
                logger.error("Insufficient funds!");
                return null;
            } else {
                User user = new User();
                user.setId(userId);
                Payment payment = new Payment();
                payment.setAmount(amount);
                payment.setUser(user);
                payment.setPaymentType(type);
                return paymentRepository.saveAndFlush(payment);
            }
        } catch (Exception e) {
            logger.error("Error while trying add Payment!", e);
            return null;
        }
    }

    @Override
    public ApiResponse<?> getUserPaymentsApiResponse(Integer userId) {
        return ApiResponseUtil.transformObjectToApiResponse(getUserPayments(userId), "balance");
    }

    @Override
    public ApiResponse<?> addPaymentApiResponse(PaymentDTO payment, String type) {
        ApiResponse<?> response = new ApiResponse<>();
        try {
            if (payment == null || payment.getUserId() == null
                    || payment.getAmount() == null) {
                logger.error("Invalid data for payment!");
                response.addErrorMessage("Insufficient funds!");
            } else if (canUserPay(payment.getUserId(), payment.getAmount())) {
                if (addPayment(payment.getUserId(), payment.getAmount(), type) == null) {
                    logger.error("Invalid data for adding payment!");
                    response.addErrorMessage("Insufficient funds!");
                } else {
                    response.addInfoMessage("Successful transaction!");
                }
            } else {
                response.addErrorMessage("Insufficient funds!");
            }
        } catch (Exception e) {
            logger.error("Error adding payment, for API call!\n" + e.getMessage());
            response.addErrorMessage("Insufficient funds!");
        }
        return response;
    }
}
