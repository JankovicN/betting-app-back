package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.service.interfaceTest.OddServiceTest;
import rs.ac.bg.fon.repository.OddRepository;
import rs.ac.bg.fon.service.OddServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OddServiceImplTest extends OddServiceTest {
    @Mock
    private OddRepository oddRepositoryMock;

    @BeforeEach
    void setUp() {
        this.oddRepository = oddRepositoryMock;
        this.oddService = new OddServiceImpl(oddRepositoryMock);
    }

    @AfterEach
    void tearDown() {
        this.oddService = null;
    }
}
