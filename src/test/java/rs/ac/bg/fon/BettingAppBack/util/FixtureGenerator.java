package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.entity.Odd;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FixtureGenerator {
    private static int fixtureCounter = 1;

    public static Fixture generateUniqueFixture() {
        Fixture fixture = new Fixture();
        fixture.setId(fixtureCounter++);
        fixture.setDate(LocalDateTime.now());
        fixture.setHome(TeamGenerator.generateUniqueTeam());
        fixture.setAway(TeamGenerator.generateUniqueTeam());
        fixture.setHomeGoals(0);
        fixture.setAwayGoals(0);
        fixture.setState(Constants.FIXTURE_NOT_STARTED);
        fixture.setOdds(generateOddsForFixture(fixture));
        return fixture;
    }

    private static List<Odd> generateOddsForFixture(Fixture fixture) {
        List<Odd> odds = OddGenerator.generateUniqueOdds(2);
        for (Odd odd : odds) {
            odd.setFixture(fixture);
        }
        return odds;
    }

    public static List<Fixture> generateUniqueFixtures(int numOfFixtures){
        List<Fixture> fixtures = new ArrayList<>();
        for (int i = 0;i<numOfFixtures;i++){
            fixtures.add(generateUniqueFixture());
        }
        return fixtures;
    }
    private static List<Fixture> setHomeTeamForFixtures(List<Fixture> fixtures) {
        for (Fixture fixture : fixtures) {
            fixture.setHome(TeamGenerator.generateUniqueTeam());
        }
        return fixtures;
    }
    private static List<Fixture> setAwayTeamForFixtures(List<Fixture> fixtures) {
        for (Fixture fixture : fixtures) {
            fixture.setAway(TeamGenerator.generateUniqueTeam());
        }
        return fixtures;
    }
    private static List<Fixture> setLeagueForFixtures(List<Fixture> fixtures, League league) {
        for (Fixture fixture : fixtures) {
            fixture.setLeague(league);
        }
        return fixtures;
    }
}
