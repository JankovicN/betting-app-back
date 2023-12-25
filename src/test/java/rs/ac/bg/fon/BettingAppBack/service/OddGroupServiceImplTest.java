package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.service.interfaceTest.OddGroupServiceTest;
import rs.ac.bg.fon.repository.OddGroupRepository;
import rs.ac.bg.fon.service.OddGroupServiceImpl;
import rs.ac.bg.fon.service.OddService;

@ExtendWith(MockitoExtension.class)
public class OddGroupServiceImplTest extends OddGroupServiceTest {
    @Mock
    private OddGroupRepository oddGroupRepositoryMock;
    @Mock
    private OddService oddServiceMock;

    @BeforeEach
    void setUp() {
        this.oddGroupRepository = oddGroupRepositoryMock;
        this.oddService = oddServiceMock;
        this.oddGroupService = new OddGroupServiceImpl(oddGroupRepository, oddService);
    }

    @AfterEach
    void tearDown() {
        this.oddGroupService = null;
    }
}
