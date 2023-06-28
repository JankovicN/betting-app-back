package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;

import java.util.List;

public interface FixtureOddsService {

    List<League> getFixturesBasic();
    Fixture getFixtureWithOdds(int fixtureId);

    League getFixturesBasicFilter(League league);
    List<Fixture> getFixturesWithOddsByLeagueId(int leagueId);
}
