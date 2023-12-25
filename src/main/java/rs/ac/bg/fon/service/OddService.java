package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.Odd;

import java.util.List;

/**
 * Represents a service layer interface responsible for defining all Odd related methods.
 *
 * @author Janko
 * @version 1.0
 */
@Service
public interface OddService {

    /**
     * Adds new odd to database. Returns instance of saved odd from database.
     *
     * @param odd instance of Odd class that is being saved.
     * @return instance of Odd class that is saved in database,
     * or null if error occurs.
     */
    Odd save(Odd odd);

    /**
     * Return Odd object with id that is specified.
     *
     * @param oddId Integer value representing id of Odd.
     * @return instance of Odd class,
     * or null if error occurs of if there is no Odd with specified id.
     */
    Odd getOddById(Integer oddId);

    /**
     * Adds list of odds to database. Returns list of saved odds from database.
     *
     * @param odds list of Odd objects are being saved.
     * @return list of Odd objects that are saved in database,
     * or empty list if error occurs.
     */
    List<Odd> saveOddList(List<Odd> odds);

    /**
     * Creates list of Odd objects for fixture and oddGroup specified.
     *
     * @param fixtureId  Integer value of fixture id for which odds are being fetched for.
     * @param oddGroupId Integer value of odd group for which odds are being fetched for.
     * @return list of Odd objects that are associated with the given fixture and odd group id,
     * or empty list if an error occurs.
     */
    List<Odd> getOddsForFixtureAndOddGroup(Integer fixtureId, Integer oddGroupId);

    /**
     * Creates and transforms list of Odd objects to list of OddDTO objects, for fixture and oddGroup specified.
     *
     * @param fixtureId  Integer value of fixture id for which odds are being fetched for.
     * @param oddGroupId Integer value of odd group for which odds are being fetched for.
     * @return list of OddDTO objects that are associated with the given fixture and odd group id,
     * or empty list if an error occurs.
     */
    List<OddDTO> createOddDTOList(Integer fixtureId, Integer oddGroupId);

    /**
     * Checks if there are any odds for fixture and odd group.
     *
     * @param oddGroupId Integer value representing id of odd group that search is based on.
     * @param fixtureId  Integer value of fixture id for which odds are being fetched for.
     * @return boolean value, return true if there is an odd for given fixture id and odd group id ,
     * otherwise return false.
     */
    boolean existsWithFixtureIdAndOddGroupId(Integer fixtureId, Integer oddGroupId);

}
