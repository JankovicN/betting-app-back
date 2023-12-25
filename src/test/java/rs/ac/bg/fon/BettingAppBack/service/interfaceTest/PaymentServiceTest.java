package rs.ac.bg.fon.BettingAppBack.service.interfaceTest;

import org.junit.jupiter.api.Test;
import rs.ac.bg.fon.BettingAppBack.util.PaymentGenerator;
import rs.ac.bg.fon.BettingAppBack.util.UserGenerator;
import rs.ac.bg.fon.dtos.Payment.PaymentDTO;
import rs.ac.bg.fon.entity.Payment;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.repository.PaymentRepository;
import rs.ac.bg.fon.service.PaymentService;
import rs.ac.bg.fon.utility.ApiResponse;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public abstract class PaymentServiceTest {

    protected PaymentService paymentService;
    protected PaymentRepository paymentRepository;

    // Test can User pay
    @Test
    void testCanUserPayPositiveAmount() {

        BigDecimal amount = BigDecimal.valueOf(500.0);
        User user = UserGenerator.generateUniqueUser();

        boolean canPay = paymentService.canUserPay(user.getId(), amount);

        assertTrue(canPay);
    }

    @Test
    void testCanUserPaySuccess() {

        Double balance = 1000.0;
        BigDecimal amount = BigDecimal.valueOf(-500.0);
        User user = UserGenerator.generateUniqueUser();
        when(paymentRepository.getUserPayments(user.getId())).thenReturn(balance);

        boolean canPay = paymentService.canUserPay(user.getId(), amount);

        assertTrue(canPay);
    }

    @Test
    void testCanUserPayInsufficientFunds() {

        Double balance = 1000.0;
        BigDecimal amount = BigDecimal.valueOf(-5000.0);
        User user = UserGenerator.generateUniqueUser();
        when(paymentRepository.getUserPayments(user.getId())).thenReturn(balance);

        boolean canPay = paymentService.canUserPay(user.getId(), amount);

        assertFalse(canPay);
    }

    @Test
    void testCanUserPayError() {

        User user = UserGenerator.generateUniqueUser();

        boolean canPay = paymentService.canUserPay(user.getId(), null);

        assertFalse(canPay);
    }


    // Test get User Payments
    @Test
    void testGetUserPaymentsSuccess() {

        Double amount = 1000.0;
        User user = UserGenerator.generateUniqueUser();

        when(paymentRepository.getUserPayments(user.getId())).thenReturn(amount);

        BigDecimal userPayments = paymentService.getUserPayments(user.getId());

        assertEquals(BigDecimal.valueOf(amount), userPayments);
    }

    @Test
    void testGetUserPaymentsNullBalanceReturned() {

        User user = UserGenerator.generateUniqueUser();

        when(paymentRepository.getUserPayments(user.getId())).thenReturn(null);

        BigDecimal userPayments = paymentService.getUserPayments(user.getId());

        assertEquals(BigDecimal.ZERO, userPayments);
    }

    @Test
    void testGetUserPaymentsDatabaseError() {

        User user = UserGenerator.generateUniqueUser();

        when(paymentRepository.getUserPayments(user.getId()))
                .thenThrow(new RuntimeException("Simulated database error!"));

        BigDecimal userPayments = paymentService.getUserPayments(user.getId());

        assertEquals(BigDecimal.ZERO, userPayments);
    }


    // Test add Payment object
    @Test
    void testAddPaymentPositiveAmountSuccess() {
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());

        when(paymentRepository.saveAndFlush(payment)).thenReturn(payment);

        Payment savedPayment = paymentService.addPayment(payment);

        assertEquals(payment, savedPayment);
        verify(paymentRepository, times(1)).saveAndFlush(payment);
    }

    @Test
    void testAddPaymentSuccess() {
        Double balance = 1000.0;
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        payment.setAmount(BigDecimal.valueOf(-500.0));

        when(paymentRepository.saveAndFlush(payment)).thenReturn(payment);

        when(paymentRepository.getUserPayments(payment.getUser().getId())).thenReturn(balance);

        Payment savedPayment = paymentService.addPayment(payment);

        assertEquals(payment, savedPayment);
        verify(paymentRepository, times(1)).saveAndFlush(payment);
    }

    @Test
    void testAddPaymentUserIsNull() {
        Payment payment = new Payment(1, BigDecimal.ONE, "type", null);

        Payment savedPayment = paymentService.addPayment(payment);

        assertNull(savedPayment);
    }

    @Test
    void testAddPaymentAmountIsNull() {
        Payment payment = new Payment(1, null, "type", new User());

        Payment savedPayment = paymentService.addPayment(payment);

        assertNull(savedPayment);
    }

    @Test
    void testAddPaymentIsNull() {
        Payment savedPayment = paymentService.addPayment(null);

        assertNull(savedPayment);
    }

    @Test
    void testAddPaymentInsufficientFunds() {
        Double balance = 1000.0;
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        payment.setAmount(BigDecimal.valueOf(-5000.0));

        when(paymentRepository.getUserPayments(payment.getUser().getId())).thenReturn(balance);

        Payment savedPayment = paymentService.addPayment(payment);

        assertNull(savedPayment);
    }

    @Test
    void testAddPaymentDatabaseError() {
        Double balance = 1000.0;
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        payment.setAmount(BigDecimal.valueOf(-500.0));

        when(paymentRepository.getUserPayments(payment.getUser().getId())).thenReturn(balance);

        when(paymentRepository.saveAndFlush(payment)).thenThrow(new RuntimeException("Simulated Exception"));

        Payment savedPayment = paymentService.addPayment(payment);

        assertNull(savedPayment);
    }


    // Test create and add Payment
    @Test
    void testCreateAndAddPaymentPositiveAmount() {
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());

        when(paymentRepository.saveAndFlush(any(Payment.class))).thenReturn(payment);

        Payment savedPayment = paymentService.addPayment(payment.getUser().getId(), payment.getAmount(), payment.getPaymentType());

        assertEquals(payment, savedPayment);
    }

    @Test
    void testCreateAndAddPaymentSuccess() {
        Double balance = 1000.0;
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        payment.setAmount(BigDecimal.valueOf(-500.0));

        when(paymentRepository.saveAndFlush(any(Payment.class))).thenReturn(payment);

        when(paymentRepository.getUserPayments(payment.getUser().getId())).thenReturn(balance);

        Payment savedPayment = paymentService.addPayment(payment.getUser().getId(), payment.getAmount(), payment.getPaymentType());

        assertEquals(payment, savedPayment);
        verify(paymentRepository, times(1)).saveAndFlush(any(Payment.class));
    }

    @Test
    void testCreateAndAddPaymentUserIdIsNull() {
        Payment payment = new Payment(1, BigDecimal.ONE, "type", new User());

        Payment savedPayment = paymentService.addPayment(payment.getUser().getId(), payment.getAmount(), payment.getPaymentType());

        assertNull(savedPayment);
    }

    @Test
    void testCreateAndAddPaymentAmountIsNull() {
        Payment payment = new Payment(1, null, "type", new User());

        Payment savedPayment = paymentService.addPayment(payment.getUser().getId(), payment.getAmount(), payment.getPaymentType());

        assertNull(savedPayment);
    }

    @Test
    void testCreateAndAddPaymentTypeIsNull() {
        Payment payment = new Payment(1, BigDecimal.ONE, null, UserGenerator.generateUniqueUser());

        Payment savedPayment = paymentService.addPayment(payment.getUser().getId(), payment.getAmount(), payment.getPaymentType());

        assertNull(savedPayment);
    }

    @Test
    void testCreateAndAddPaymentInsufficientFunds() {
        Double balance = 1000.0;
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        payment.setAmount(BigDecimal.valueOf(-5000.0));

        when(paymentRepository.getUserPayments(payment.getUser().getId())).thenReturn(balance);

        Payment savedPayment = paymentService.addPayment(payment.getUser().getId(), payment.getAmount(), payment.getPaymentType());

        assertNull(savedPayment);
    }

    @Test
    void testCreateAndAddPaymentDatabaseError() {
        Double balance = 1000.0;
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        payment.setAmount(BigDecimal.valueOf(-500.0));

        when(paymentRepository.getUserPayments(payment.getUser().getId())).thenReturn(balance);

        when(paymentRepository.saveAndFlush(any(Payment.class))).thenThrow(new RuntimeException("Simulated Exception"));

        Payment savedPayment = paymentService.addPayment(payment.getUser().getId(), payment.getAmount(), payment.getPaymentType());

        assertNull(savedPayment);
    }


    // Test add Payment object API Response
    @Test
    void testAddPaymentApiResponsePositiveAmount() {
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        PaymentDTO paymentDTO = new PaymentDTO(payment.getUser().getId(), payment.getAmount());

        when(paymentRepository.saveAndFlush(any(Payment.class))).thenReturn(payment);

        ApiResponse<?> apiResponse = paymentService.addPaymentApiResponse(paymentDTO, payment.getPaymentType());

        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successful transaction!", apiResponse.getInfoMessages().get(0));
    }

    @Test
    void testAddPaymentApiResponseSuccess() {
        Double balance = 1000.0;
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        payment.setAmount(BigDecimal.valueOf(-500.0));
        PaymentDTO paymentDTO = new PaymentDTO(payment.getUser().getId(), BigDecimal.valueOf(-500.0));

        when(paymentRepository.saveAndFlush(any(Payment.class))).thenReturn(payment);

        when(paymentRepository.getUserPayments(paymentDTO.getUserId())).thenReturn(balance);

        ApiResponse<?> apiResponse = paymentService.addPaymentApiResponse(paymentDTO, payment.getPaymentType());

        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successful transaction!", apiResponse.getInfoMessages().get(0));
    }

    @Test
    void testAddPaymentApiResponseUserIdIsNull() {
        PaymentDTO paymentDTO = new PaymentDTO(null, BigDecimal.valueOf(-500.0));

        ApiResponse<?> apiResponse = paymentService.addPaymentApiResponse(paymentDTO, PaymentGenerator.generateRandomPaymentType());

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Insufficient funds!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testAddPaymentApiResponseAmountIsNull() {
        User user = UserGenerator.generateUniqueUser();
        PaymentDTO paymentDTO = new PaymentDTO(user.getId(), null);

        ApiResponse<?> apiResponse = paymentService.addPaymentApiResponse(paymentDTO, PaymentGenerator.generateRandomPaymentType());

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Insufficient funds!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testAddPaymentApiResponseTypeIsNull() {
        Double balance = 1000.0;
        Payment payment = new Payment(1, BigDecimal.ONE, null, UserGenerator.generateUniqueUser());
        PaymentDTO paymentDTO = new PaymentDTO(payment.getUser().getId(), BigDecimal.valueOf(-500.0));

        when(paymentRepository.getUserPayments(paymentDTO.getUserId())).thenReturn(balance);

        ApiResponse<?> apiResponse = paymentService.addPaymentApiResponse(paymentDTO, null);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Insufficient funds!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testAddPaymentApiResponseInsufficientFunds() {
        Double balance = 1000.0;
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        payment.setAmount(BigDecimal.valueOf(-5000.0));
        PaymentDTO paymentDTO = new PaymentDTO(payment.getUser().getId(), payment.getAmount());

        when(paymentRepository.getUserPayments(payment.getUser().getId())).thenReturn(balance);

        ApiResponse<?> apiResponse = paymentService.addPaymentApiResponse(paymentDTO, PaymentGenerator.generateRandomPaymentType());

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Insufficient funds!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testAddPaymentApiResponseDatabaseError() {
        Double balance = 1000.0;
        Payment payment = PaymentGenerator.generateUniquePayment(UserGenerator.generateUniqueUser());
        payment.setAmount(BigDecimal.valueOf(-500.0));

        when(paymentRepository.getUserPayments(payment.getUser().getId())).thenReturn(balance);

        when(paymentRepository.saveAndFlush(any(Payment.class))).thenThrow(new RuntimeException("Simulated Exception"));

        Payment savedPayment = paymentService.addPayment(payment.getUser().getId(), payment.getAmount(), payment.getPaymentType());

        assertNull(savedPayment);
    }

    // Test get User Payments API Response
    @Test
    void testGetUserPaymentsApiResponseSuccess() {

        Double amount = 1000.0;
        User user = UserGenerator.generateUniqueUser();

        when(paymentRepository.getUserPayments(user.getId())).thenReturn(amount);

        ApiResponse<?> userPaymentsApiResponse = paymentService.getUserPaymentsApiResponse(user.getId());

        assertEquals(BigDecimal.valueOf(amount), userPaymentsApiResponse.getData());
    }

    @Test
    void testGetUserPaymentsApiResponseNullBalanceReturned() {

        User user = UserGenerator.generateUniqueUser();

        when(paymentRepository.getUserPayments(user.getId())).thenReturn(null);

        ApiResponse<?> userPaymentsApiResponse = paymentService.getUserPaymentsApiResponse(user.getId());

        assertEquals(BigDecimal.ZERO, userPaymentsApiResponse.getData());
    }

    @Test
    void testGetUserPaymentsApiResponseDatabaseError() {

        User user = UserGenerator.generateUniqueUser();

        when(paymentRepository.getUserPayments(user.getId()))
                .thenThrow(new RuntimeException("Simulated database error!"));

        ApiResponse<?> userPaymentsApiResponse = paymentService.getUserPaymentsApiResponse(user.getId());

        assertEquals(BigDecimal.ZERO, userPaymentsApiResponse.getData());
    }

}
