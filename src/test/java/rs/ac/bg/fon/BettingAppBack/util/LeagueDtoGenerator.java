package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.League.LeagueDTO;
import rs.ac.bg.fon.dtos.Team.TeamDTO;

import java.util.ArrayList;
import java.util.Arrays;

public class LeagueDtoGenerator {
    private static int leagueDtoCounter = 1;

    public static LeagueDTO generateUniqueLeagueDTO() {
        LeagueDTO leagueDTO = new LeagueDTO();
        leagueDTO.setId(leagueDtoCounter++);
        leagueDTO.setName(generateUniqueLeagueName());
        leagueDTO.setFixtures(Arrays.asList(new FixtureDTO(), new FixtureDTO()));
        return leagueDTO;
    }

    public static String generateUniqueLeagueName() {
        return "LeagueDTO" + leagueDtoCounter;
    }
}
