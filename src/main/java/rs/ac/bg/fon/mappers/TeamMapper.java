package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Team;

@Component
public class TeamMapper{
    public TeamDTO teamToTeamDTO(Team team) throws Exception {
        if(team.getId()==null || team.getId()<0 || team.getName()==null || team.getName().isBlank()){
            throw new Exception("Team object has invalid fields [id = "+team.getId()+", name = "+team.getName()+"]");
        }
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());
        return teamDTO;
    }
}
