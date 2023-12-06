package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.OddGroup.OddGroupDTO;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.utility.Utility;

import java.util.List;

@Component
public class FixtureMapper {


    public static FixtureDTO fixtureToFixtureDTO(Fixture fixture) throws Exception {
        if (fixture.getId() == null || fixture.getId() < 0 || fixture.getDate() == null || fixture.getState() == null || fixture.getState().isBlank()) {
            throw new Exception("Fixture object has invalid fields [id = " + fixture.getId() + ", state = " + fixture.getState() + ", date = " + fixture.getDate() + "]");
        }

        FixtureDTO fixtureDTO = new FixtureDTO();
        fixtureDTO.setId(fixture.getId());
        fixtureDTO.setDate(Utility.formatDateTime(fixture.getDate()));
        fixtureDTO.setState(fixture.getState());
        return fixtureDTO;
    }


    public static FixtureDTO fixtureToFixtureDTO(Fixture fixture, TeamDTO home, TeamDTO away, List<OddGroupDTO> oddGroupDTO) throws Exception {
        if (fixture.getHomeGoals() < 0 || fixture.getAwayGoals() < 0) {
            throw new Exception("Fixture object has invalid fields [homeGoals = " + fixture.getHomeGoals() + ", awayGoals = " + fixture.getAwayGoals() + "]");
        } else if (home == null || away == null) {
            throw new Exception("Team object is null [home = " + home + ", away = " + away + "]");
        } else if (oddGroupDTO == null) {
            throw new Exception("List of Odd Groups is null!");
        }

        FixtureDTO fixtureDTO = fixtureToFixtureDTO(fixture);
        fixtureDTO.setHomeGoals(fixture.getHomeGoals());
        fixtureDTO.setAwayGoals(fixture.getAwayGoals());
        fixtureDTO.setHome(home);
        fixtureDTO.setAway(away);
        fixtureDTO.setOddGroupList(oddGroupDTO);
        return fixtureDTO;
    }
}
