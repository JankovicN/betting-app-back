package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Team;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FixtureTest {

    Fixture fixture;

    @BeforeEach
    void setUp(){
        fixture = new Fixture();
    }
    @AfterEach
    void tearDown() {
        fixture = null;
    }

    @Test
    void testFixtureDefaultValues() {
        assertNull(fixture.getId());
        assertNull(fixture.getDate());
        assertNull(fixture.getHome());
        assertNull(fixture.getAway());
        assertNull(fixture.getLeague());
        assertEquals(new ArrayList<>(), fixture.getOdds());
        assertEquals(Constants.FIXTURE_NOT_STARTED, fixture.getState());
        assertEquals(0, fixture.getHomeGoals());
        assertEquals(0, fixture.getAwayGoals());
    }

    @Test
    void testFixtureAllArgsConstructor() {
        LocalDateTime date = LocalDateTime.now();
        Team home = new Team();
        Team away = new Team();
        League league = new League();
        List<Odd> odds = new ArrayList<>();
        odds.add(new Odd());
        fixture = new Fixture(2, date, home, away, 2, 2, Constants.FIXTURE_NOT_STARTED, league, odds);

        assertEquals(2, fixture.getId());
        assertEquals(date, fixture.getDate());
        assertEquals(home, fixture.getHome());
        assertEquals(away, fixture.getAway());
        assertEquals(2, fixture.getHomeGoals());
        assertEquals(2, fixture.getAwayGoals());
        assertEquals(Constants.FIXTURE_NOT_STARTED, fixture.getState());
        assertEquals(league, fixture.getLeague());
        assertEquals(odds, fixture.getOdds());
    }
    @Test
    void testFixtureToString() {
        Team home = new Team();
        home.setName("Arsenal");
        Team away = new Team();
        away.setName("Brighton");
        fixture.setHome(home);
        fixture.setAway(away);

        assertEquals("Arsenal - Brighton", fixture.toString());
    }

    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetFixtureId(Integer id) {
        fixture.setId(id);
        assertEquals(id, fixture.getId());
    }
    @Test
    void testSetFixtureIdThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> fixture.setId(null));
        String errorMessage = "Fixture ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetFixtureDate() {
        LocalDateTime date = LocalDateTime.now();
        fixture.setDate(date);
        assertEquals(date, fixture.getDate());
    }
    @Test
    void testSetFixtureDateThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> fixture.setDate(null));
        String errorMessage = "Fixture date can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetHomeTeamForFixture() {
        Team home = new Team();
        fixture.setHome(home);
        assertEquals(home, fixture.getHome());
    }
    @Test
    void testSetHomeTeamForFixtureThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> fixture.setHome(null));
        String errorMessage = "Home team can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }
    @ParameterizedTest
    @CsvSource({"2", "3", "1", "6"})
    void testSetHomeTeamGoalsForFixture(int numOfGoals) {
        fixture.setHomeGoals(numOfGoals);
        assertEquals(numOfGoals, fixture.getHomeGoals());
    }
    @Test
    void testSetInvalidHomeTeamGoalsForFixture() {
        int numOfGoals = -1;
        fixture.setHomeGoals(numOfGoals);
        assertEquals(0, fixture.getHomeGoals());
    }

    @Test
    void testSetAwayTeamForFixture() {
        Team away = new Team();
        fixture.setAway(away);
        assertEquals(away, fixture.getAway());
    }
    @Test
    void testSetAwayTeamForFixtureThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> fixture.setAway(null));
        String errorMessage = "Away team can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }
    @ParameterizedTest
    @CsvSource({"2", "3", "1", "6"})
    void testSetAwayTeamGoalsForFixture(int numOfGoals) {
        fixture.setAwayGoals(numOfGoals);
        assertEquals(numOfGoals, fixture.getAwayGoals());
    }
    @Test
    void testSetInvalidAwayTeamGoalsForFixture() {
        int numOfGoals = -1;
        fixture.setAwayGoals(numOfGoals);
        assertEquals(0, fixture.getAwayGoals());
    }

    @ParameterizedTest
    @CsvSource({Constants.FIXTURE_NOT_STARTED, Constants.FIXTURE_IN_PLAY, Constants.FIXTURE_FULL_TIME})
    void testSetFixtureState(String state) {
        fixture.setState(state);
        assertEquals(state, fixture.getState());
    }
    @Test
    void testSetFixtureStateThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> fixture.setState(null));
        String errorMessage = "Fixture state can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetFixtureLeague() {
        League league = new League();
        fixture.setLeague(league);
        assertEquals(league, fixture.getLeague());
    }
    @Test
    void testSetFixtureLeagueThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> fixture.setLeague(null));
        String errorMessage = "League can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetFixtureOdds() {
        List<Odd> odds = new ArrayList<>();
        odds.add(new Odd());
        fixture.setOdds(odds);
        assertEquals(odds, fixture.getOdds());
    }
    @Test
    void testSetFixtureOddsToNull() {
        fixture.setOdds(null);
        assertEquals(new ArrayList<>(), fixture.getOdds());
    }
    @Test
    void testFixtureOddsAreNotSet() {
        assertEquals(new ArrayList<>(), fixture.getOdds());
    }

}
