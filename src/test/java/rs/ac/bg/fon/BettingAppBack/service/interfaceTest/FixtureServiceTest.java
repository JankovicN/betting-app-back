package rs.ac.bg.fon.BettingAppBack.service.interfaceTest;

import org.junit.jupiter.api.Test;
import rs.ac.bg.fon.BettingAppBack.util.FixtureGenerator;
import rs.ac.bg.fon.BettingAppBack.util.LeagueGenerator;
import rs.ac.bg.fon.BettingAppBack.util.TeamDTOGenerator;
import rs.ac.bg.fon.BettingAppBack.util.TeamGenerator;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.OddGroup.OddGroupDTO;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.entity.Team;
import rs.ac.bg.fon.repository.FixtureRepository;
import rs.ac.bg.fon.service.FixtureService;
import rs.ac.bg.fon.service.OddGroupService;
import rs.ac.bg.fon.service.TeamService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public abstract class FixtureServiceTest {

    protected FixtureService fixtureService;
    protected FixtureRepository fixtureRepository;
    protected OddGroupService oddGroupService;
    protected TeamService teamService;

    @Test
    void testSaveFixtureSuccess() {
        Fixture fixture = new Fixture();
        when(fixtureRepository.save(fixture)).thenReturn(fixture);

        Fixture savedFixture = fixtureService.save(fixture);
        assertEquals(savedFixture, fixture);
    }

    @Test
    void testSaveFixtureShouldReturnNull() {
        when(fixtureRepository.save(null)).thenThrow(new NullPointerException());

        Fixture savedFixture = fixtureService.save(null);
        assertNull(savedFixture);
    }

    @Test
    void testFindFixtureByIdSuccess() {
        Fixture fixture = FixtureGenerator.generateUniqueFixture();
        when(fixtureRepository.findById(fixture.getId())).thenReturn(Optional.of(fixture));

        Fixture foundFixture = fixtureService.getFixtureById(fixture.getId());
        assertEquals(foundFixture, fixture);
    }

    @Test
    void testFindFixtureByIdDatabaseError() {
        int fixtureID = 1;
        when(fixtureRepository.findById(fixtureID)).thenThrow(new NullPointerException());

        Fixture foundFixture = fixtureService.getFixtureById(fixtureID);
        assertNull(foundFixture);
    }

    @Test
    void testFindFixtureByIdFixtureNotPresent() {
        int fixtureID = 1;
        when(fixtureRepository.findById(fixtureID)).thenReturn(Optional.empty());

        Fixture foundFixture = fixtureService.getFixtureById(fixtureID);
        assertNull(foundFixture);
    }

    @Test
    void testGetNotStartedByLeagueSuccess() {
        League league = LeagueGenerator.generateUniqueLeague();
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);

        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(Constants.FIXTURE_NOT_STARTED, league.getId()))
                .thenReturn(fixtures);
        when(teamService.findById(anyInt())).thenReturn(Optional.of(TeamGenerator.generateUniqueTeam()));

        List<Fixture> foundFixtures = fixtureService.getNotStartedByLeague(league.getId());
        assertEquals(fixtures.size(), foundFixtures.size());
    }

    @Test
    void testGetNotStartedByLeagueDatabaseError() {
        League league = LeagueGenerator.generateUniqueLeague();
        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(Constants.FIXTURE_NOT_STARTED, league.getId()))
                .thenThrow(new NullPointerException());
        List<Fixture> foundFixture = fixtureService.getNotStartedByLeague(league.getId());
        assertEquals(new ArrayList<>(), foundFixture);
    }

    @Test
    void testGetNotStartedByLeagueNoFixtures() {
        League league = LeagueGenerator.generateUniqueLeague();
        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(Constants.FIXTURE_NOT_STARTED, league.getId()))
                .thenReturn(null);
        List<Fixture> foundFixture = fixtureService.getNotStartedByLeague(league.getId());
        assertEquals(new ArrayList<>(), foundFixture);
    }

    @Test
    void testGetFixtureDtoByLeagueSuccess() {
        League league = LeagueGenerator.generateUniqueLeague();
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);

        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(anyString(), anyInt()))
                .thenReturn(fixtures);

        when(teamService.findById(anyInt()))
                .thenReturn(Optional.of(TeamGenerator.generateUniqueTeam()));

        when(teamService.createTeamDTO(any(Team.class)))
                .thenReturn(TeamDTOGenerator.generateUniqueTeamDTO());

        when(oddGroupService.createOddGroupDTOList(anyInt(), anyInt()))
                .thenReturn(Arrays.asList(new OddGroupDTO(), new OddGroupDTO()));

        List<FixtureDTO> foundFixtureDTOS = fixtureService.getFixtureDtoByLeague(league.getId());

        assertEquals(2, foundFixtureDTOS.size());
    }

    @Test
    void testGetFixtureDtoByLeagueErrorGettingFixtures() {
        League league = LeagueGenerator.generateUniqueLeague();
        when(fixtureService.getNotStartedByLeague(league.getId()))
                .thenThrow(new NullPointerException());
        List<FixtureDTO> foundFixture = fixtureService.getFixtureDtoByLeague(league.getId());
        assertEquals(new ArrayList<>(), foundFixture);
    }

    @Test
    void testGetFixtureDtoByLeagueErrorCreatingFixtureDTOList() {
        League league = LeagueGenerator.generateUniqueLeague();
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);

        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(anyString(), anyInt()))
                .thenReturn(fixtures);

        when(teamService.findById(anyInt()))
                .thenReturn(Optional.of(TeamGenerator.generateUniqueTeam()));

        when(fixtureService.createFixtureDTOList(fixtures))
                .thenThrow(new NullPointerException());

        List<FixtureDTO> foundFixture = fixtureService.getFixtureDtoByLeague(league.getId());
        assertEquals(new ArrayList<>(), foundFixture);
    }

    @Test
    void testGetFixtureDtoByLeagueOddGroupListIsEmpty() {
        League league = LeagueGenerator.generateUniqueLeague();
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);

        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(anyString(), anyInt()))
                .thenReturn(fixtures);

        when(teamService.findById(anyInt()))
                .thenReturn(Optional.of(TeamGenerator.generateUniqueTeam()));

        when(oddGroupService.createOddGroupDTOList(anyInt(), anyInt()))
                .thenReturn(new ArrayList<>());

        List<FixtureDTO> foundFixtureDTOS = fixtureService.getFixtureDtoByLeague(league.getId());

        assertEquals(new ArrayList<>(), foundFixtureDTOS);
    }

    @Test
    void testGetFixtureDtoByLeagueErrorCreatingTeamDTO() {
        League league = LeagueGenerator.generateUniqueLeague();
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);

        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(anyString(), anyInt()))
                .thenReturn(fixtures);

        when(teamService.findById(anyInt()))
                .thenReturn(Optional.of(TeamGenerator.generateUniqueTeam()));

        when(teamService.createTeamDTO(any(Team.class)))
                .thenReturn(null);

        when(oddGroupService.createOddGroupDTOList(anyInt(), anyInt()))
                .thenReturn(Arrays.asList(new OddGroupDTO(), new OddGroupDTO()));

        List<FixtureDTO> foundFixtureDTOS = fixtureService.getFixtureDtoByLeague(league.getId());

        assertEquals(new ArrayList<>(), foundFixtureDTOS);
    }

    @Test
    void testCreateFixtureDTOListSuccess() {
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);

        when(teamService.findById(anyInt()))
                .thenReturn(Optional.of(TeamGenerator.generateUniqueTeam()));

        when(teamService.createTeamDTO(any(Team.class)))
                .thenReturn(TeamDTOGenerator.generateUniqueTeamDTO());

        when(oddGroupService.createOddGroupDTOList(anyInt(), anyInt()))
                .thenReturn(Arrays.asList(new OddGroupDTO(), new OddGroupDTO()));

        List<FixtureDTO> foundFixtureDTOS = fixtureService.createFixtureDTOList(fixtures);

        assertEquals(2, foundFixtureDTOS.size());
    }

    @Test
    void testCreateFixtureDTOListErrorCreatingOddGroupList() {
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);

        when(teamService.findById(anyInt()))
                .thenReturn(Optional.of(TeamGenerator.generateUniqueTeam()));

        when(oddGroupService.createOddGroupDTOList(anyInt(), anyInt()))
                .thenReturn(new ArrayList<>());

        List<FixtureDTO> foundFixtureDTOS = fixtureService.createFixtureDTOList(fixtures);

        assertEquals(0, foundFixtureDTOS.size());
        assertEquals(new ArrayList<>(), foundFixtureDTOS);
    }

    @Test
    void testCreateFixtureDTOListErrorCreatingTeamDTO() {
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);

        when(teamService.findById(anyInt()))
                .thenReturn(Optional.of(TeamGenerator.generateUniqueTeam()));

        when(teamService.createTeamDTO(any(Team.class)))
                .thenReturn(null);

        when(oddGroupService.createOddGroupDTOList(anyInt(), anyInt()))
                .thenReturn(Arrays.asList(new OddGroupDTO(), new OddGroupDTO()));

        List<FixtureDTO> foundFixtureDTOS = fixtureService.createFixtureDTOList(fixtures);

        assertEquals(0, foundFixtureDTOS.size());
        assertEquals(new ArrayList<>(), foundFixtureDTOS);
    }

    @Test
    void testCreateFixtureDTOListErrorIsThrown() {
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);


        when(oddGroupService.createOddGroupDTOList(anyInt(), anyInt()))
                .thenThrow(new NullPointerException());

        List<FixtureDTO> foundFixtureDTOS = fixtureService.createFixtureDTOList(fixtures);

        assertEquals(0, foundFixtureDTOS.size());
        assertEquals(new ArrayList<>(), foundFixtureDTOS);
    }

    @Test
    void testExistFixtureByLeagueIdSuccess() {
        League league = LeagueGenerator.generateUniqueLeague();
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);

        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(Constants.FIXTURE_NOT_STARTED, league.getId()))
                .thenReturn(fixtures);
        when(teamService.findById(anyInt())).thenReturn(Optional.of(TeamGenerator.generateUniqueTeam()));

        assertTrue(fixtureService.existFixtureByLeagueId(league.getId()));
    }

    @Test
    void testExistFixtureByLeagueIdErrorIsThrown() {
        League league = LeagueGenerator.generateUniqueLeague();

        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(Constants.FIXTURE_NOT_STARTED, league.getId()))
                .thenThrow(new NullPointerException());

        assertFalse(fixtureService.existFixtureByLeagueId(league.getId()));
    }

    @Test
    void testExistFixtureByLeagueIdNoFixturesFound() {
        League league = LeagueGenerator.generateUniqueLeague();

        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(Constants.FIXTURE_NOT_STARTED, league.getId()))
                .thenReturn(new ArrayList<>());


        assertFalse(fixtureService.existFixtureByLeagueId(league.getId()));
    }

    @Test
    void testExistFixtureByLeagueIdNoTeamFound() {
        League league = LeagueGenerator.generateUniqueLeague();

        when(fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(Constants.FIXTURE_NOT_STARTED, league.getId()))
                .thenReturn(new ArrayList<>());

        assertFalse(fixtureService.existFixtureByLeagueId(league.getId()));
    }
}
