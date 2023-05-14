package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Payment;

@Service
public interface PaymentService {

    Double getUserPayments(int userId);

    void addPayment(Payment payment);
}
