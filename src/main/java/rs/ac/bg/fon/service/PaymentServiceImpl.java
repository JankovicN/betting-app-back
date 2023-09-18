package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Payment;
import rs.ac.bg.fon.repository.PaymentRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;
    UserService userService;
    TicketService ticketService;

    @Override
    public boolean canUserPay(Integer userId, Double amount) {
        if(amount>=0.0){
            return true;
        }
        return getUserPayments(userId).compareTo(BigDecimal.valueOf(amount)) >= 0;
    }

    @Override
    public boolean canUserPay(Integer userId, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO)>=0){
            return true;
        }
        return getUserPayments(userId).compareTo(amount) >= 0;
    }

    @Override
    public BigDecimal getUserPayments(Integer userId) {
        return BigDecimal.valueOf(paymentRepository.getUserPayments(userId));
    }

    @Override
    public void addPayment(Payment payment) {
        if(canUserPay(payment.getUser().getId(), payment.getAmount())) {
            paymentRepository.saveAndFlush(payment);
        }else{
            //TODO add logging
        }
    }

    @Override
    public void addPayment(Integer userId, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setUser(userService.getUser(userId));
        paymentRepository.saveAndFlush(payment);
    }

    @Override
    public void addPayment(Integer userId, BigDecimal amount, String type) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setUser(userService.getUser(userId));
        payment.setPaymentType(type);
        paymentRepository.saveAndFlush(payment);
    }

    @Override
    public ApiResponse<?> getUserPaymentsApiResponse(Integer userId) {
        return ApiResponseUtil.transformObjectToApiResponse(getUserPayments(userId).add(ticketService.getTotalWinAmountForUser(userId)).subtract(ticketService.getWagerAmoutForUser(userId)), "balance");
    }

    @Override
    public ApiResponse<?> addPaymentApiResponse(Integer userId, Double amount) {
        ApiResponse<?> response = new ApiResponse<>();
        if (canUserPay(userId, amount)) {
            addPayment(userId, BigDecimal.valueOf(amount));
            response.addInfoMessage("Successful payment!");
        } else {
            response.addErrorMessage("Insufficient funds!");
        }
        return response;
    }

    @Override
    public ApiResponse<?> addPaymentApiResponse(Integer userId, Double amount, String type) {
        ApiResponse<?> response = new ApiResponse<>();
        if (canUserPay(userId, amount)) {
            addPayment(userId, BigDecimal.valueOf(amount), type);
            response.addInfoMessage("Successful payment!");
        } else {
            response.addErrorMessage("Insufficient funds!");
        }
        return response;
    }


    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
