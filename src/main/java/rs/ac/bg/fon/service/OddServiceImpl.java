package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.mappers.OddMapper;
import rs.ac.bg.fon.repository.OddRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a service layer class responsible for implementing all Odd related methods.
 *
 * @author Janko
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OddServiceImpl implements OddService {

    /**
     * Instance of Logger class, responsible for displaying messages that contain information about the success of methods inside Odd service class.
     */
    private static final Logger logger = LoggerFactory.getLogger(OddServiceImpl.class);

    /**
     * Instance of Odd repository class, responsible for interacting with odd table in database.
     */
    private final OddRepository oddRepository;

    /**
     * Adds new odd to database. Returns instance of saved odd from database.
     *
     * @param odd instance of Odd class that is being saved.
     * @return instance of Odd class that is saved in database,
     * or null if error occurs.
     */
    @Override
    public Odd save(Odd odd) {
        try {
            Odd savedOdd = oddRepository.saveAndFlush(odd);
            logger.info("Successfully saved Odd " + savedOdd + "!");
            return savedOdd;
        } catch (Exception e) {
            logger.error("Error while trying to save Odd " + odd + "!\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Return Odd object with id that is specified.
     *
     * @param oddId Integer value representing id of Odd.
     * @return instance of Odd class,
     * or null if error occurs of if there is no Odd with specified id.
     */
    @Transactional
    @Override
    public Odd getOddById(Integer oddId) {
        try {
            Optional<Odd> odd = oddRepository.findById(oddId);
            if (odd.isPresent()) {
                logger.info("Successfully found Odd with id: " + oddId + ", odd:  \n" + odd.get() + "!");
                Odd fetchedOdd = odd.get();
                fetchedOdd.getFixture();
                return fetchedOdd;
            }
            return null;
        } catch (Exception e) {
            logger.error("Error while trying to find Odd with id =  " + oddId + "!\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Adds list of odds to database. Returns list of saved odds from database.
     *
     * @param odds list of Odd objects are being saved.
     * @return list of Odd objects that are saved in database,
     * or empty list if error occurs.
     */
    @Override
    public List<Odd> saveOddList(List<Odd> odds) {
        try {
            List<Odd> savedOdds = oddRepository.saveAllAndFlush(odds);
            logger.info("Successfully saved list of Odds!");
            return savedOdds;
        } catch (Exception e) {
            logger.error("Error while trying to save list of Odds!!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Creates list of Odd objects for fixture and oddGroup specified.
     *
     * @param fixtureId  Integer value of fixture id for which odds are being fetched for.
     * @param oddGroupId Integer value of odd group for which odds are being fetched for.
     * @return list of Odd objects that are associated with the given fixture and odd group id,
     * or empty list if an error occurs.
     */
    @Override
    public List<Odd> getOddsForFixtureAndOddGroup(Integer fixtureId, Integer oddGroupId) {
        try {
            List<Odd> oddList = oddRepository.findByFixtureStateAndFixtureIdAndOddGroupId(Constants.FIXTURE_NOT_STARTED, fixtureId, oddGroupId);
            logger.info("Successfully found list of Odds for fixtureId = " + fixtureId + " and oddGroupId = " + oddGroupId + "!\n" + "Odd List: " + oddList);

            return oddList == null ? new ArrayList<>() : oddList;
        } catch (Exception e) {
            logger.error("Error while trying to find list of Odds!!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Creates and transforms list of Odd objects to list of OddDTO objects, for fixture and oddGroup specified.
     *
     * @param fixtureId  Integer value of fixture id for which odds are being fetched for.
     * @param oddGroupId Integer value of odd group for which odds are being fetched for.
     * @return list of OddDTO objects that are associated with the given fixture and odd group id,
     * or empty list if an error occurs.
     */
    @Override
    public List<OddDTO> createOddDTOList(Integer fixtureId, Integer oddGroupId) {

        List<OddDTO> oddDTOList = new ArrayList<>();
        try {
            List<Odd> oddList = getOddsForFixtureAndOddGroup(fixtureId, oddGroupId);
            for (Odd odd : oddList) {
                try {
                    OddDTO oddDTO = OddMapper.oddToOddDTO(odd);
                    oddDTOList.add(oddDTO);
                } catch (Exception e) {
                    logger.error("Error while trying to map Odd to OddDTO!\n" + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Error while trying to save list of Odds!\n" + e.getMessage());
            return new ArrayList<>();
        }
        return oddDTOList;
    }

    /**
     * Checks if there are any odds for fixture and odd group.
     *
     * @param oddGroupId Integer value representing id of odd group that search is based on.
     * @param fixtureId  Integer value of fixture id for which odds are being fetched for.
     * @return boolean value, return true if there is an odd for given fixture id and odd group id ,
     * otherwise return false.
     */
    @Override
    public boolean existsWithFixtureIdAndOddGroupId(Integer fixtureId, Integer oddGroupId) {

        try {
            return oddRepository.existsByFixtureIdAndOddGroupId(fixtureId, oddGroupId);
        } catch (Exception e) {
            logger.error("Error while trying to check if Odds exists for Fixture ID = " + fixtureId + " and Odd Group ID = " + oddGroupId + "!\n" + e.getMessage());
            return false;
        }
    }
}
