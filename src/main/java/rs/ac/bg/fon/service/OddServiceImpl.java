package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.mappers.OddMapper;
import rs.ac.bg.fon.repository.OddRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OddServiceImpl implements OddService {
    private static final Logger logger = LoggerFactory.getLogger(OddServiceImpl.class);

    private OddRepository oddRepository;
    private OddMapper oddMapper;

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

    @Override
    public List<Odd> saveOddList(List<Odd> odds) {
        try {
            List<Odd> savedOdds = oddRepository.saveAllAndFlush(odds);
            logger.info("Successfully saved list of Odds!");
            return savedOdds;
        } catch (Exception e) {
            logger.error("Error while trying to save list of Odds!!\n" + e.getMessage());
            return null;
        }
    }


    @Override
    public List<Odd> getOddsForFixtureAndBetGroup(Integer fixtureId, Integer betGroupId) {
        try {
            List<Odd> oddList = oddRepository.findByFixtureStateAndFixtureIdAndBetGroupId(Constants.FIXTURE_NOT_STARTED, fixtureId, betGroupId);
            logger.info("Successfully found list of Odds!");
            return oddList;
        } catch (Exception e) {
            logger.error("Error while trying to save list of Odds!!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }


    @Override
    public List<OddDTO> createOddDTOList(Integer fixtureId, Integer betGroupId) {

        List<OddDTO> oddDTOList = new ArrayList<>();
        try {
            List<Odd> oddList = getOddsForFixtureAndBetGroup(fixtureId, betGroupId);
            for (Odd odd : oddList) {
                try {
                    OddDTO oddDTO = oddMapper.oddToOddDTO(odd);
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
    public boolean existsWithFixtureIdAndBetGroupId(Integer fixtureId, Integer betGroupId) {

        try {
            return oddRepository.existsByFixtureIdAndBetGroupId(fixtureId, betGroupId);
        } catch (Exception e) {
            logger.error("Error unnecessary while trying to check if Odds exits for Fixture ID = " + fixtureId + " and Bet Group ID = " + betGroupId + "!\n" + e.getMessage());
            return false;
        }
    }


    @Autowired
    public void setOddMapper(OddMapper oddMapper) {
        this.oddMapper = oddMapper;
    }

    @Autowired
    public void setOddRepository(OddRepository oddRepository) {
        this.oddRepository = oddRepository;
    }

}
