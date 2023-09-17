package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.BetGroup.BetGroupDTO;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Fixture;

import java.util.List;

@Component
public class
FixtureMapper {

    public FixtureDTO fixtureToFixtureDTO(Fixture fixture) throws Exception {
        if (fixture.getId() == null || fixture.getId() < 0 || fixture.getDate() == null || fixture.getState() == null || fixture.getState().isBlank()) {
            throw new Exception("Fixture object has invalid fields [id = " + fixture.getId() + ", state = " + fixture.getState() + ", date = " + fixture.getDate() + "]");
        }

        FixtureDTO fixtureDTO = new FixtureDTO();
        fixtureDTO.setId(fixture.getId());
        fixtureDTO.setDate(fixture.getDate());
        fixtureDTO.setState(fixture.getState());
        return fixtureDTO;
    }


    public FixtureDTO fixtureToFixtureDTO(Fixture fixture, TeamDTO home, TeamDTO away, List<BetGroupDTO> betGroupDTO) throws Exception {
        if (fixture.getHomeGoals() < 0 || fixture.getAwayGoals() < 0) {
            throw new Exception("Fixture object has invalid fields [homeGoals = " + fixture.getHomeGoals() + ", awayGoals = " + fixture.getAwayGoals() + "]");
        } else if (home == null || away == null) {
            throw new Exception("Team object is null [home = " + home + ", away = " + away + "]");
        } else if (betGroupDTO == null) {
            throw new Exception("List of bet groups is null!");
        }

        FixtureDTO fixtureDTO = fixtureToFixtureDTO(fixture);
        fixtureDTO.setHomeGoals(fixture.getHomeGoals());
        fixtureDTO.setAwayGoals(fixture.getAwayGoals());
        fixtureDTO.setHome(home);
        fixtureDTO.setAway(away);
        fixtureDTO.setBetGroupList(betGroupDTO);
        return fixtureDTO;
    }
}
