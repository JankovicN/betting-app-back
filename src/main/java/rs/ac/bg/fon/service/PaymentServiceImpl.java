package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Payment;
import rs.ac.bg.fon.repository.PaymentRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService{

    private PaymentRepository paymentRepository;

    @Override
    public Double getUserPayments(int userId){
        return paymentRepository.getUserPayments(userId);
    }

    @Override
    public void addPayment(Payment payment) {
        paymentRepository.saveAndFlush(payment);
    }


    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
