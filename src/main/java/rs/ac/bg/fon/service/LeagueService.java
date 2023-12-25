package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;


/**
 * Represents a service layer interface responsible for defining all League related methods.
 * Available API method implementations: GET
 *
 * @author Janko
 * @version 1.0
 */
public interface LeagueService {

    /**
     * Adds new league to database. Returns instance of saved league from database.
     *
     * @param league instance of League class that is being saved.
     * @return instance of League class that is saved in database,
     * or null if error occurs.
     */
    League save(League league);

    /**
     * Adds list of leagues to database. Returns list of saved leagues from database.
     *
     * @param leagues list of League objects are being saved.
     * @return list of League objects that are saved in database,
     * or null if error occurs.
     */
    List<League> saveLeagues(List<League> leagues);

    /**
     * Return list of all leagues that are in database.
     *
     * @return list of League objects that are in database,
     * or empty list if an error occurs.
     */
    List<League> getAllLeagues();

    /**
     * Checks if league table has any rows, in other word if there are any leagues saved so far.
     *
     * @return boolean value, return true if league table is not empty,
     * otherwise return false.
     */
    boolean exists();

    /**
     * Returns response for API call, containing list of all leagues, where each league contains fixtures that have not started yet.
     *
     * @return instance of ApiResponse class,
     * containing list of League objects, each one containing fixtures which have not started yet.
     */
    ApiResponse<?> getAllLeaguesWithFixturesApiResponse();

    /**
     * Returns response for API call, containing list of leagues that have at least one fixture that has not started.
     *
     * @return instance of ApiResponse class,
     * containing list of League objects.
     */
    ApiResponse<?> getAllLeaguesApiResponse();

    /**
     * Returns response for API call, containing LeagueDTO object along with list of FixtureDTO objects for that league.
     *
     * @param leagueId Integer value representing id of League that search is based on.
     * @return instance of ApiResponse class containing LeagueDTO object with list of FixtureDTO objects that are associated with that league,
     * if there is an error, then ApiResponse object with error message is returned instead.
     */
    ApiResponse<?> getNotStartedByLeagueApiCall(Integer leagueId);
}
