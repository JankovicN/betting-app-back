package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.entity.Payment;
import rs.ac.bg.fon.entity.User;

import java.math.BigDecimal;

public class PaymentGenerator {
    private static int paymentCounter = 1;

    public static Payment generateUniquePayment(User user) {
        Payment payment = new Payment();
        payment.setId(paymentCounter++);
        payment.setAmount(generateRandomAmount());
        payment.setPaymentType(generateRandomPaymentType());
        payment.setUser(user);
        return payment;
    }

    private static BigDecimal generateRandomAmount() {
        // Implement logic to generate a random payment amount
        // For simplicity, this example uses a counter-based approach
        return new BigDecimal(paymentCounter * 100);
    }

    public static String generateRandomPaymentType() {
        // Implement logic to generate a random payment type
        // For simplicity, this example uses a counter-based approach
        return "PaymentType" + paymentCounter;
    }
}
