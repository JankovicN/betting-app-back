package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.Fixture;

import java.time.LocalDateTime;
import java.util.List;


public interface FixtureService {

    Fixture save(Fixture fixture);
    Fixture getFixtureById(int fixtureId);

    List<Fixture> getNotStarted();

    boolean existsByDate(LocalDateTime date);

    List<Fixture> getNotStartedByLeague(int league);
}
