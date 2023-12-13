package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Team;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {
    Team team;

    @BeforeEach
    void setUp() {
        team = new Team();
    }

    @AfterEach
    void tearDown() {
        team = null;
    }

    @Test
    void testTeamDefaultValues() {
        assertNull(team.getId());
        assertNull(team.getName());
        assertEquals(new ArrayList<>(), team.getHome());
        assertEquals(new ArrayList<>(), team.getAway());
    }

    @Test
    void testTeamAllArgsConstructor() {
        List<Fixture> home = new ArrayList<>();
        home.add(new Fixture());
        List<Fixture> away = new ArrayList<>();
        away.add(new Fixture());
        team = new Team(2, "West Ham", home, away);

        assertEquals(2, team.getId());
        assertEquals("West Ham", team.getName());
        assertEquals(home, team.getHome());
        assertEquals(away, team.getAway());
    }

    @Test
    void testTeamToString() {
        team.setName("Barcelona");

        assertEquals("Barcelona", team.toString());
    }

    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetTeamId(Integer id) {
        team.setId(id);
        assertEquals(id, team.getId());
    }

    @Test
    void testSetTeamIdThrowsNullPointerException() {
        NullPointerException nullPointerException = assertThrows(java.lang.NullPointerException.class, () -> team.setId(null));
        String errorMessage = "Team ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"Real Madrid", "Bayern", "Juventus"})
    void testSetTeamName(String name) {
        team.setName(name);
        assertEquals(name, team.getName());
    }

    @Test
    void testSetTeamNameThrowsNullPointerException() {
        NullPointerException nullPointerException = assertThrows(java.lang.NullPointerException.class, () -> team.setName(null));
        String errorMessage = "Team name can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetHomeTeamFixtures() {
        List<Fixture> homeFixtures = new ArrayList<>();
        homeFixtures.add(new Fixture());
        team.setHome(homeFixtures);
        assertEquals(homeFixtures, team.getHome());
    }

    @Test
    void testSetHomeTeamFixturesToNull() {
        team.setHome(null);
        assertEquals(new ArrayList<>(), team.getHome());
    }

    @Test
    void testHomeTeamFixturesAreNotSet() {
        assertEquals(new ArrayList<>(), team.getHome());
    }

    @Test
    void testSetAwayTeamFixtures() {
        List<Fixture> awayFixtures = new ArrayList<>();
        awayFixtures.add(new Fixture());
        team.setAway(awayFixtures);
        assertEquals(awayFixtures, team.getAway());
    }

    @Test
    void testSetAwayTeamFixturesToNull() {
        team.setAway(null);
        assertEquals(new ArrayList<>(), team.getAway());
    }

    @Test
    void testAwayTeamFixturesAreNotSet() {
        assertEquals(new ArrayList<>(), team.getAway());
    }
}
