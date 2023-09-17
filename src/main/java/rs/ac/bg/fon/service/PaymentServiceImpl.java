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
    public BigDecimal getUserPayments(Integer userId) {
        return BigDecimal.valueOf(paymentRepository.getUserPayments(userId));
    }

    @Override
    public void addPayment(Payment payment) {
        paymentRepository.saveAndFlush(payment);
    }

    @Override
    public void addPayment(Integer userId, Double amount) {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(amount));
        payment.setUser(userService.getUser(userId));
        paymentRepository.saveAndFlush(payment);

    }

    @Override
    public ApiResponse<?> getUserPaymentsApiResponse(Integer userId) {
        return ApiResponseUtil.transformObjectToApiResponse(getUserPayments(userId).add(ticketService.getTotalWinAmountForUser(userId)).subtract(ticketService.getWagerAmoutForUser(userId)), "balance");
    }

    @Override
    public ApiResponse<?> addPaymentApiResponse(Integer userId, Double amount) {
        ApiResponse<?> response = new ApiResponse<>();
        if (getUserPayments(userId).compareTo(BigDecimal.valueOf(amount)) < 0) {
            response.addErrorMessage("Insufficient funds!");
        } else {
            addPayment(userId, amount);
            response.addInfoMessage("Successful payment!");
        }
        return response;
    }


    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
