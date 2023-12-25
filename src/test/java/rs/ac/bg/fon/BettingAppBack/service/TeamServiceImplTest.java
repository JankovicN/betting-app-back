package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.service.interfaceTest.TeamServiceTest;
import rs.ac.bg.fon.repository.TeamRepository;
import rs.ac.bg.fon.service.TeamServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest extends TeamServiceTest {

    @Mock
    private TeamRepository teamRepositoryMock;

    @BeforeEach
    void setUp() {
        this.teamRepository = teamRepositoryMock;
        this.teamService = new TeamServiceImpl(teamRepository);
    }

    @AfterEach
    void tearDown() {
        this.teamService = null;
    }
}
