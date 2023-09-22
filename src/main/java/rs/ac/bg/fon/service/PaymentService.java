package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Payment.PaymentDTO;
import rs.ac.bg.fon.entity.Payment;
import rs.ac.bg.fon.utility.ApiResponse;

import java.math.BigDecimal;

@Service
public interface PaymentService {

    BigDecimal getUserPayments(Integer userId);

    void addPayment(Payment payment);


    void addPayment(Integer userId, BigDecimal amount, String type);


    boolean canUserPay(Integer userId, BigDecimal amount);

    ApiResponse<?> getUserPaymentsApiResponse(Integer userId);

    ApiResponse<?> addPaymentApiResponse(PaymentDTO payment, String type);

}
