package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.entity.Fixture;

import java.util.List;


/**
 * Represents a service layer interface responsible for defining all Fixture related methods.
 *
 * @author Janko
 * @version 1.0
 */
public interface FixtureService {

    /**
     * Adds new fixture to database. Returns instance of saved fixture from database.
     *
     * @param fixture instance of Fixture class that is being saved.
     * @return instance of Fixture class that is saved in database,
     * or null if error occurs.
     */
    Fixture save(Fixture fixture);

    /**
     * Return Fixture object for id that is specified.
     *
     * @param fixtureID Integer value representing id of Fixture.
     * @return instance of Fixture class,
     * or null if error occurs or if there is no fixture with specified id.
     */
    Fixture getFixtureById(Integer fixtureID);

    /**
     * Return list of fixtures that have not started and are contained in specified league.
     *
     * @param leagueID Integer value representing id of league which contains fixtures.
     * @return list of Fixture objects that have not started and are contained in league with specified id,
     * or empty list if an error occurs.
     */
    List<Fixture> getNotStartedByLeague(Integer leagueID);

    /**
     * Return list of fixtures DTOs that are contained in specified league.
     *
     * @param leagueID Integer value representing id of league which contains fixtures.
     * @return list of FixtureDTO objects that are contained in league with specified id,
     * or empty list if an error occurs.
     */
    List<FixtureDTO> getFixtureDtoByLeague(Integer leagueID);

    /**
     * Transforms and returns list of FixtureDTO objects.
     *
     * @param fixtures list of Fixture objects.
     * @return list of FixtureDTO objects that are representing list of Fixture objects,
     * or empty list if an error occurs.
     */
    List<FixtureDTO> createFixtureDTOList(List<Fixture> fixtures);

    /**
     * Checks if there are any fixtures that have not started for a league with id that is specified.
     *
     * @param leagueId Integer value representing id of league which should be checked.
     * @return boolean value, return true if League contains fixtures that have not started and have valid odds related to them,
     * otherwise return false.
     */
    boolean existFixtureByLeagueId(Integer leagueId);
}
