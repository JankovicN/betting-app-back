package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.entity.Payment;
import rs.ac.bg.fon.entity.User;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
    }

    @AfterEach
    void tearDown() {
        payment = null;
    }

    @Test
    void testPaymentDefaultValues() {
        assertNull(payment.getId());
        assertNull(payment.getAmount());
        assertNull(payment.getPaymentType());
        assertNull(payment.getUser());
    }

    @Test
    void testPaymentAllArgsConstructor() {
        User user = new User();
        payment = new Payment(2, BigDecimal.valueOf(2000.0), Constants.PAYMENT_DEPOSIT, user);

        assertEquals(2, payment.getId());
        assertEquals(BigDecimal.valueOf(2000.0), payment.getAmount());
        assertEquals(Constants.PAYMENT_DEPOSIT, payment.getPaymentType());
        assertEquals(user, payment.getUser());
    }

    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetPaymentId(Integer id) {
        payment.setId(id);
        assertEquals(id, payment.getId());
    }
    @Test
    void testSetPaymentIdThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> payment.setId(null));
        String errorMessage = "Payment ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"200.5", "3000.85", "-200.0", "-8000.0"})
    void testSetPaymentAmount(BigDecimal paymentAmount) {
        payment.setAmount(paymentAmount);
        assertEquals(paymentAmount, payment.getAmount());
    }
    @Test
    void testSetPaymentAmountThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> payment.setAmount(null));
        String errorMessage = "Payment amount can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({Constants.PAYMENT_DEPOSIT, Constants.PAYMENT_PAYOUT, Constants.PAYMENT_REFUND, Constants.PAYMENT_WAGER, Constants.PAYMENT_WITHDRAW})
    void testSetPaymentType(String paymentType) {
        payment.setPaymentType(paymentType);
        assertEquals(paymentType, payment.getPaymentType());
    }
    @Test
    void testSetPaymentTypeThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> payment.setPaymentType(null));
        String errorMessage = "Payment type can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetPaymentUser() {
        User user = new User();
        payment.setUser(user);
        assertEquals(user, payment.getUser());
    }
    @Test
    void testSetPaymentUserThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> payment.setUser(null));
        String errorMessage = "User for payment can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

}
