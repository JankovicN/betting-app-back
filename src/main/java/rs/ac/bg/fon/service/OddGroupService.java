package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dtos.OddGroup.OddGroupDTO;
import rs.ac.bg.fon.entity.OddGroup;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;


/**
 * Represents a service layer interface responsible for defining all Odd Group related methods.
 * Available API method implementations: GET, DELETE
 *
 * @author Janko
 * @version 1.0
 */
public interface OddGroupService {

    /**
     * Returns response for API call, containing list of odd groups and their odds for fixture.
     *
     * @param fixtureID Integer value representing id of fixture that fetching is based on.
     * @return instance of ApiResponse class, containing list of OddGroupDTO objects.
     */
    ApiResponse<?> getOddGroupsByFixtureApiResponse(Integer fixtureID);

    /**
     * Adds new odd group to database. Returns instance of saved odd group from database.
     *
     * @param oddGroup instance of OddGroup class that is being saved.
     * @return instance of OddGroup class that is saved in database,
     * or null if error occurs.
     */
    OddGroup saveOddGroup(OddGroup oddGroup);

    /**
     * Adds list of odd groups to database. Returns list of saved odd group from database.
     *
     * @param oddGroups list of OddGroup objects are being saved.
     * @return list of OddGroup objects that are saved in database,
     * or empty list if error occurs.
     */
    List<OddGroup> saveOddGroups(List<OddGroup> oddGroups);

    /**
     * Return OddGroup object with id that is specified.
     *
     * @param oddGroupId Integer value representing id of Odd Group.
     * @return instance of OddGroup class,
     * or null if error occurs of if there is no Odd Group with specified id.
     */
    OddGroup getOddGroupWithId(Integer oddGroupId);

    /**
     * Return list of OddGroupDTO objects that are associated with provided fixture.
     *
     * @param fixtureID Integer value representing id of fixture that odds are fetched for.
     * @return list of OddGroupDTO objects that are contained in fixture with specified id,
     * or empty list if an error occurs.
     */
    List<OddGroupDTO> getOddGroupsByFixture(Integer fixtureID);

    /**
     * Checks if odd_group table has any rows, in other word if there are any odd groups saved so far.
     *
     * @return boolean value, return true if odd_group table is not empty,
     * otherwise return false.
     */
    boolean exists();

    /**
     * Checks if there is an odd group with specified id
     *
     * @param oddGroupId Integer value representing id of odd group that search is based on.
     * @return boolean value, return true if there is an odd group with that id,
     * otherwise return false.
     */
    boolean existsWithId(Integer oddGroupId);

    /**
     * Deletes OddGroup row from database with id that is specified.
     *
     * @param oddGroupId Integer value representing id of Odd Group to be deleted.
     * @throws Exception if there is and error while executing the delete query.
     */
    void deleteOddGroup(Integer oddGroupId) throws Exception;

    /**
     * Creates and transforms list of OddGroup objects to list of OddGroupDTO objects.
     *
     * @param fixtureID  Integer value of fixture id for which odds are being fetched for.
     * @param oddGroupID Integer value of odd group for which odds are being fetched for.
     * @return list of OddGroupDTO objects that are associated with the given fixture and odd group id,
     * or empty list if an error occurs.
     */
    List<OddGroupDTO> createOddGroupDTOList(Integer fixtureID, Integer oddGroupID);

    /**
     * Returns response for API call with messages indicating the success of  Odd Group deletion from database.
     *
     * @param oddGroupID Integer value representing id of Odd Group to be deleted.
     * @return instance of ApiResponse class,
     * containing messages indicating the success of Odd Group deletion from the database.
     */
    ApiResponse<?> deleteOddGroupApiResponse(Integer id);
}
