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
