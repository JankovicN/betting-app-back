package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;


public interface FixtureService {

    Fixture save(Fixture fixture);

    Fixture getFixtureById(Integer fixtureID);

    List<Fixture> getNotStartedByLeague(Integer league);

    List<FixtureDTO> getFixtureDtoByLeague(Integer leagueID);

    List<FixtureDTO> createFixtureDTOList(List<Fixture> fixtures);

//    ApiResponse<?> getNotStartedByLeagueApiCall(Integer leagueID);
//
//    ApiResponse<?> getNotStartedByLeaguesApiCall(List<Integer> leagues);

    boolean existFixtureByLeagueId(Integer leagueId);
}
