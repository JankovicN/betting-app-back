package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.service.interfaceTest.TicketServiceTest;
import rs.ac.bg.fon.repository.TicketRepository;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.service.PaymentService;
import rs.ac.bg.fon.service.TicketServiceImpl;
import rs.ac.bg.fon.service.UserService;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest extends TicketServiceTest {

    @Mock
    private TicketRepository ticketRepositoryMock;

    @Mock
    private BetService betServiceMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private PaymentService paymentServiceMock;

    @BeforeEach
    void setUp() {
        this.ticketRepository = ticketRepositoryMock;
        this.betService = betServiceMock;
        this.userService = userServiceMock;
        this.paymentService = paymentServiceMock;
        this.ticketService = new TicketServiceImpl(ticketRepository, paymentService, userService, betService);
    }

    @AfterEach
    void tearDown() {
        this.ticketService = null;
    }
}
