package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.entity.Fixture;

import java.util.List;


public interface FixtureService {

    Fixture save(Fixture fixture);

    Fixture getFixtureById(Integer fixtureID);

    List<Fixture> getNotStartedByLeague(Integer league);

    List<FixtureDTO> getFixtureDtoByLeague(Integer leagueID);

    List<FixtureDTO> createFixtureDTOList(List<Fixture> fixtures);

    boolean existFixtureByLeagueId(Integer leagueId);
}
