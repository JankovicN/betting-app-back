package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;

import java.util.ArrayList;
import java.util.List;

public class LeagueGenerator {
    private static int leagueCounter = 1;

    public static League generateUniqueLeague() {
        League league = new League();
        league.setId(leagueCounter++);
        league.setName(generateUniqueLeagueName());
        league.setFixtures(generateFixturesForLeague(league));
        return league;
    }

    public static List<League> generateUniqueLeagues(int numOfLeagues){
        List<League> leagues = new ArrayList<>();
        for (int i = 0;i<numOfLeagues;i++){
            leagues.add(generateUniqueLeague());
        }
        return leagues;
    }
    private static String generateUniqueLeagueName() {
        return "league" + leagueCounter;
    }

    private static List<Fixture> generateFixturesForLeague(League league) {
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);
        for (Fixture fixture : fixtures) {
            fixture.setLeague(league);
        }
        return fixtures;
    }
}
