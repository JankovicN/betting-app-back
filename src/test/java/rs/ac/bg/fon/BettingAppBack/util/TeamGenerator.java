package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Team;

import java.util.List;

public class TeamGenerator {
    private static int teamCounter = 1;

    public static Team generateUniqueTeam() {
        Team team = new Team();
        team.setId(teamCounter++);
        team.setName(generateUniqueTeamName());
        return team;
    }

    public static String generateUniqueTeamName() {
        return "Team" + teamCounter;
    }

    private static List<Fixture> generateFixturesForHomeTeam(Team team) {
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);
        for (Fixture fixture : fixtures) {
            fixture.setHome(team);
        }
        return fixtures;
    }
    private static List<Fixture> generateFixturesForAwayTeam(Team team) {
        List<Fixture> fixtures = FixtureGenerator.generateUniqueFixtures(2);
        for (Fixture fixture : fixtures) {
            fixture.setAway(team);
        }
        return fixtures;
    }
}
