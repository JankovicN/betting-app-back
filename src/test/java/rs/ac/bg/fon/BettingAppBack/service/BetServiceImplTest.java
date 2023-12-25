package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.service.interfaceTest.BetServiceTest;
import rs.ac.bg.fon.repository.BetRepository;
import rs.ac.bg.fon.service.BetServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BetServiceImplTest extends BetServiceTest {

    @Mock
    private BetRepository betRepositoryMock;

    @BeforeEach
    void setUp() {
        this.betRepository = betRepositoryMock;
        this.betService = new BetServiceImpl(betRepositoryMock);
    }

    @AfterEach
    void tearDown() {
        this.betService = null;
    }
}
