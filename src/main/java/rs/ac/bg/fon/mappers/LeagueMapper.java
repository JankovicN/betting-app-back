package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.League.LeagueBasicDTO;
import rs.ac.bg.fon.dtos.League.LeagueDTO;
import rs.ac.bg.fon.entity.League;

import java.util.ArrayList;
import java.util.List;

@Component
public class LeagueMapper {

    public LeagueDTO leagueToLeagueDTO(League league) throws Exception {
        if (league.getId() == null || league.getId() < 0 || league.getName() == null || league.getName().isBlank()) {
            throw new Exception("League object has invalid fields [id = " + league.getId() + ", name = " + league.getName() + "]");
        }
        LeagueDTO leagueDTO = new LeagueDTO();
        leagueDTO.setId(league.getId());
        leagueDTO.setName(league.getName());
        return leagueDTO;
    }

    public LeagueDTO leagueToLeagueDTO(League league, List<FixtureDTO> fixutreDTOList) throws Exception {
        if (fixutreDTOList == null) {
            throw new Exception("Fixture list is null!");
        }
        LeagueDTO leagueDTO = leagueToLeagueDTO(league);
        leagueDTO.setFixtures(fixutreDTOList);
        return leagueDTO;
    }

    public LeagueBasicDTO leagueToLeagueBasicDTO(League league) throws Exception {
        if (league.getId() == null || league.getId() < 0 || league.getName() == null || league.getName().isBlank()) {
            throw new Exception("League object has invalid fields [id = " + league.getId() + ", name = " + league.getName() + "]");
        }
        LeagueBasicDTO leagueDTO = new LeagueBasicDTO(league.getId(), league.getName());
        return leagueDTO;
    }

    public List<LeagueBasicDTO> leagueListToLeagueBasicDTOList(List<League> leagueList) throws Exception {
        List<LeagueBasicDTO> leagueBasicDTOS = new ArrayList<>();
        for (League league : leagueList) {
            LeagueBasicDTO leagueDTO = leagueToLeagueBasicDTO(league);
            leagueBasicDTOS.add(leagueDTO);
        }
        return leagueBasicDTOS;
    }
}
