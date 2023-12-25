package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.service.interfaceTest.LeagueServiceTest;
import rs.ac.bg.fon.repository.LeagueRepository;
import rs.ac.bg.fon.service.FixtureService;
import rs.ac.bg.fon.service.LeagueServiceImpl;

@ExtendWith(MockitoExtension.class)
public class LeagueServiceImplTest extends LeagueServiceTest {

    @Mock
    private LeagueRepository leagueRepositoryMock;
    @Mock
    private FixtureService fixtureServiceMock;

    @BeforeEach
    void setUp() {
        this.leagueRepository = leagueRepositoryMock;
        this.fixtureService = fixtureServiceMock;
        this.leagueService = new LeagueServiceImpl(leagueRepository, fixtureService);
    }

    @AfterEach
    void tearDown() {
        this.leagueService = null;
    }
}
