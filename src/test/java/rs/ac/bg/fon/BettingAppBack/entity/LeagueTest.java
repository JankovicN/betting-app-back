package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeagueTest {

    League league;
    @BeforeEach
    void setUp(){
        league = new League();
    }
    @AfterEach
    void tearDown() {
        league = null;
    }

    @Test
    void testLeagueDefaultValues() {
        assertNull(league.getId());
        assertNull(league.getName());
        assertEquals(new ArrayList<>(), league.getFixtures());
    }

    @Test
    void testLeagueAllArgsConstructor() {
        List<Fixture> fixtures = new ArrayList<>();
        fixtures.add(new Fixture());
        league = new League(2, "Premier League", fixtures);

        assertEquals(2, league.getId());
        assertEquals("Premier League", league.getName());
        assertEquals(fixtures, league.getFixtures());
    }
    @Test
    void testLeagueToString() {
        league.setName("Premier League");

        assertEquals("Premier League", league.toString());
    }

    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetLeagueId(Integer id) {
        league.setId(id);
        assertEquals(id, league.getId());
    }
    @Test
    void testSetLeagueIdThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> league.setId(null));
        String errorMessage = "League ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"Premier League", "Super Lig"})
    void testSetLeagueName(String name) {
        league.setName(name);
        assertEquals(name, league.getName());
    }
    @Test
    void testSetLeagueNameThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> league.setName(null));
        String errorMessage = "League name can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetLeagueFixtures() {
        List<Fixture> fixtures = new ArrayList<>();
        fixtures.add(new Fixture());
        league.setFixtures(fixtures);
        assertEquals(fixtures, league.getFixtures());
    }
    @Test
    void testSetLeagueFixturesToNull() {
        league.setFixtures(null);
        assertEquals(new ArrayList<>(), league.getFixtures());
    }
    @Test
    void testLeagueFixturesAreNotSet() {
        assertEquals(new ArrayList<>(), league.getFixtures());
    }
}
