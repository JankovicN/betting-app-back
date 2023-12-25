package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.service.interfaceTest.FixtureServiceTest;
import rs.ac.bg.fon.repository.FixtureRepository;
import rs.ac.bg.fon.service.FixtureServiceImpl;
import rs.ac.bg.fon.service.OddGroupService;
import rs.ac.bg.fon.service.TeamService;

@ExtendWith(MockitoExtension.class)
public class FixtureServiceImplTest extends FixtureServiceTest {

    @Mock
    private FixtureRepository fixtureRepositoryMock;
    @Mock
    private OddGroupService oddGroupServiceMock;
    @Mock
    private TeamService teamServiceMock;


    @BeforeEach
    void setUp() {
        this.fixtureRepository = fixtureRepositoryMock;
        this.oddGroupService = oddGroupServiceMock;
        this.teamService = teamServiceMock;
        this.fixtureService = new FixtureServiceImpl(fixtureRepository, oddGroupService, teamService);
    }

    @AfterEach
    void tearDown() {
        this.fixtureService = null;
    }
}
