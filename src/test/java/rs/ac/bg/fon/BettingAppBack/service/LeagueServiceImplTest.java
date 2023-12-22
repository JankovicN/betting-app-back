package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.util.FixtureGenerator;
import rs.ac.bg.fon.BettingAppBack.util.LeagueGenerator;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.League.LeagueBasicDTO;
import rs.ac.bg.fon.dtos.League.LeagueDTO;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.repository.LeagueRepository;
import rs.ac.bg.fon.service.FixtureService;
import rs.ac.bg.fon.service.LeagueServiceImpl;
import rs.ac.bg.fon.service.OddGroupService;
import rs.ac.bg.fon.service.TeamService;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LeagueServiceImplTest {

    @InjectMocks
    private LeagueServiceImpl leagueService;
    @Mock
    private LeagueRepository leagueRepository;
    @Mock
    private FixtureService fixtureService;

    // Test save League
    @Test
    void testSaveSaveLeagueSuccess() {
        League league = LeagueGenerator.generateUniqueLeague();
        when(leagueRepository.save(league)).thenReturn(league);

        League savedLeague = leagueService.save(league);
        assertEquals(savedLeague, league);
    }

    @Test
    void testSaveLeagueShouldReturnNull() {
        when(leagueRepository.save(null)).thenThrow(new NullPointerException());

        League savedLeague = leagueService.save(null);
        assertNull(savedLeague);
    }


    // Test save League List
    @Test
    void testSaveSaveLeaguesSuccess() {
        List<League> leagues = LeagueGenerator.generateUniqueLeagues(2);
        when(leagueRepository.saveAll(leagues)).thenReturn(leagues);

        List<League> savedLeagues = leagueService.saveLeagues(leagues);
        assertEquals(savedLeagues, leagues);
    }

    @Test
    void testSaveLeaguesShouldReturnEmptyList() {
        when(leagueRepository.saveAll(null)).thenThrow(new NullPointerException());

        List<League> savedLeagues = leagueService.saveLeagues(null);
        assertEquals(new ArrayList<>(), savedLeagues);
    }


    // Test get all Leagues
    @Test
    void testGetAllLeaguesSuccess() {
        List<League> leagues = LeagueGenerator.generateUniqueLeagues(2);
        when(leagueRepository.findAll()).thenReturn(leagues);

        List<League> savedLeagues = leagueService.getAllLeagues();
        assertEquals(savedLeagues, leagues);
    }

    @Test
    void testGetAllLeaguesShouldReturnNull() {
        when(leagueRepository.findAll()).thenThrow(new NullPointerException());

        List<League> savedLeagues = leagueService.getAllLeagues();
        assertNull(savedLeagues);
    }


    // Test is League table empty
    @Test
    void testExistsTrue() {
        when(leagueRepository.count()).thenReturn(1L);

        boolean exists = leagueService.exists();
        assertTrue(exists);
    }

    @Test
    void testExistsFalse() {
        when(leagueRepository.count()).thenReturn(0L);

        boolean exists = leagueService.exists();
        assertFalse(exists);
    }


    // Test get all Leagues with fixtures API response
    @Test
    void testGetAllLeaguesWithFixturesApiResponseSuccess() {
        List<League> leagues = LeagueGenerator.generateUniqueLeagues(2);
        when(leagueRepository.findAll()).thenReturn(leagues);

        when(fixtureService.getNotStartedByLeague(anyInt())).thenReturn(FixtureGenerator.generateUniqueFixtures(2));

        when(fixtureService.createFixtureDTOList(anyList())).thenReturn(Arrays.asList(new FixtureDTO(), new FixtureDTO()));

        ApiResponse<?> response = leagueService.getAllLeaguesWithFixturesApiResponse();
        assertEquals(2, ((List<LeagueDTO>) response.getData()).size());
    }

    @Test
    void testGetAllLeaguesWithFixturesApiResponseNoLeaguesFound() {
        when(leagueRepository.findAll()).thenReturn(new ArrayList<>());

        ApiResponse<?> response = leagueService.getAllLeaguesWithFixturesApiResponse();
        assertEquals(0, ((List<LeagueDTO>) response.getData()).size());
        assertEquals(1, response.getInfoMessages().size());
        assertEquals("There are no leagues to show!", response.getInfoMessages().get(0));
    }

    @Test
    void testGetAllLeaguesWithFixturesApiResponseSomeLeaguesAreMissing() {
        when(leagueRepository.findAll()).thenReturn(Arrays.asList(LeagueGenerator.generateUniqueLeague()
                , null, LeagueGenerator.generateUniqueLeague()));

        when(fixtureService.getNotStartedByLeague(anyInt())).thenReturn(FixtureGenerator.generateUniqueFixtures(2));

        when(fixtureService.createFixtureDTOList(anyList())).thenReturn(Arrays.asList(new FixtureDTO(), new FixtureDTO()));

        ApiResponse<?> response = leagueService.getAllLeaguesWithFixturesApiResponse();
        assertEquals(2, ((List<LeagueDTO>) response.getData()).size());
    }

    @Test
    void testGetAllLeaguesWithFixturesApiResponseSomeNoFixtures() {
        List<League> leagues = LeagueGenerator.generateUniqueLeagues(2);
        when(leagueRepository.findAll()).thenReturn(leagues);

        when(fixtureService.getNotStartedByLeague(anyInt())).thenReturn(new ArrayList<>());

        ApiResponse<?> response = leagueService.getAllLeaguesWithFixturesApiResponse();
        assertEquals(0, ((List<LeagueDTO>) response.getData()).size());
        assertEquals(1, response.getInfoMessages().size());
        assertEquals("There are no leagues to show!", response.getInfoMessages().get(0));
    }

    @Test
    void testGetAllLeaguesWithFixturesApiResponseErrorCreatingFixtureDTOList() {
        when(leagueRepository.findAll()).thenReturn(Arrays.asList(LeagueGenerator.generateUniqueLeague()
                , null, LeagueGenerator.generateUniqueLeague()));

        when(fixtureService.getNotStartedByLeague(anyInt())).thenReturn(FixtureGenerator.generateUniqueFixtures(2));

        when(fixtureService.createFixtureDTOList(anyList())).thenReturn(new ArrayList<>());

        ApiResponse<?> response = leagueService.getAllLeaguesWithFixturesApiResponse();
        assertEquals(0, ((List<LeagueDTO>) response.getData()).size());
        assertEquals(1, response.getInfoMessages().size());
        assertEquals("There are no leagues to show!", response.getInfoMessages().get(0));
    }


    // Test get all Leagues API response
    @Test
    void testGetAllLeaguesApiResponseSuccess() {
        List<League> leagues = LeagueGenerator.generateUniqueLeagues(2);
        when(leagueRepository.findAll()).thenReturn(leagues);
        when(fixtureService.existFixtureByLeagueId(anyInt())).thenReturn(true);

        ApiResponse<?> response = leagueService.getAllLeaguesApiResponse();
        assertEquals(2, ((List<LeagueBasicDTO>)response.getData()).size());
    }

    @Test
    void testGetAllLeaguesApiResponseNoLeaguesFound() {
        when(leagueRepository.findAll()).thenReturn(new ArrayList<>());

        ApiResponse<?> response = leagueService.getAllLeaguesApiResponse();
        assertEquals(0, ((List<LeagueBasicDTO>)response.getData()).size());
        assertEquals(1, response.getInfoMessages().size());
        assertEquals("There are no leagues to show!", response.getInfoMessages().get(0));
    }

    @Test
    void testGetAllLeaguesApiResponseNoFixturesForLeague() {
        List<League> leagues = LeagueGenerator.generateUniqueLeagues(2);
        when(leagueRepository.findAll()).thenReturn(leagues);
        when(fixtureService.existFixtureByLeagueId(anyInt())).thenReturn(false);

        ApiResponse<?> response = leagueService.getAllLeaguesApiResponse();
        assertEquals(0, ((List<LeagueBasicDTO>)response.getData()).size());
        assertEquals(1, response.getInfoMessages().size());
        assertEquals("There are no leagues to show!", response.getInfoMessages().get(0));
    }


    // Test get all Leagues that have fixtures that have not started API response
    @Test
    void testGetNotStartedByLeagueApiCallSuccess(){
        when(leagueRepository.findById(anyInt())).thenReturn(Optional.of(LeagueGenerator.generateUniqueLeague()));

        when(fixtureService.getFixtureDtoByLeague(anyInt())).thenReturn(Arrays.asList(new FixtureDTO(), new FixtureDTO()));

        ApiResponse<?> response = leagueService.getNotStartedByLeagueApiCall(anyInt());
        assertNotNull(response.getData());
        assertEquals(new ArrayList<>(), response.getInfoMessages());
        assertEquals(new ArrayList<>(), response.getErrorMessages());
    }

    @Test
    void testGetNotStartedByLeagueApiCallInvalidLeagueId(){
        int leagueId = anyInt();
        when(leagueRepository.findById(leagueId)).thenReturn(Optional.empty());

        ApiResponse<?> response = leagueService.getNotStartedByLeagueApiCall(anyInt());
        assertNull(response.getData());
        assertEquals(1, response.getErrorMessages().size());
        assertEquals("No League is found for id = "+leagueId+"!", response.getErrorMessages().get(0));
    }

    @Test
    void testGetNotStartedByLeagueApiCallNoFixturesFoundForLeague(){
        League league = LeagueGenerator.generateUniqueLeague();
        when(leagueRepository.findById(league.getId())).thenReturn(Optional.of(league));

        when(fixtureService.getFixtureDtoByLeague(league.getId())).thenReturn(new ArrayList<>());

        ApiResponse<?> response = leagueService.getNotStartedByLeagueApiCall(league.getId());
        assertNull(response.getData());
        assertEquals(1, response.getErrorMessages().size());
        assertEquals("Unable to get Fixtures for League " + league.getName(), response.getErrorMessages().get(0));
    }

    @Test
    void testGetNotStartedByLeagueApiCallErrorIsThrown(){
        League league = LeagueGenerator.generateUniqueLeague();
        when(leagueRepository.findById(league.getId())).thenReturn(Optional.of(league));

        when(fixtureService.getFixtureDtoByLeague(league.getId())).thenThrow(new NullPointerException());

        ApiResponse<?> response = leagueService.getNotStartedByLeagueApiCall(league.getId());
        assertNull(response.getData());
        assertEquals(1, response.getErrorMessages().size());
        assertEquals("Unknown error!", response.getErrorMessages().get(0));
    }
}
