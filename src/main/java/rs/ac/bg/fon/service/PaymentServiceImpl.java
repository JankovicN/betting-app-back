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

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

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

    @Override
    public ApiResponse<?> getUserPaymentsApiResponse(Integer userId) {
        return ApiResponseUtil.transformObjectToApiResponse(getUserPayments(userId), "balance");
    }

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
