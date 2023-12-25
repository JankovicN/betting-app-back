package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.service.interfaceTest.PaymentServiceTest;
import rs.ac.bg.fon.repository.PaymentRepository;
import rs.ac.bg.fon.service.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest extends PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepositoryMock;

    @BeforeEach
    void setUp() {
        this.paymentRepository = paymentRepositoryMock;
        this.paymentService = new PaymentServiceImpl(paymentRepositoryMock);
    }

    @AfterEach
    void tearDown() {
        this.paymentService = null;
    }
}
