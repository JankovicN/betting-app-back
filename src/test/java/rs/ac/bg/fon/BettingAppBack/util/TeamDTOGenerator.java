package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.dtos.Team.TeamDTO;

public class TeamDTOGenerator {
    private static int teamDtoCounter = 1;

    public static TeamDTO generateUniqueTeamDTO() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(teamDtoCounter++);
        teamDTO.setName(generateUniqueTeamName());
        return teamDTO;
    }

    public static String generateUniqueTeamName() {
        return "TeamDTO" + teamDtoCounter;
    }
}
