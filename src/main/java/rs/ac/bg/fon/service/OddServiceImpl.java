package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
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

        logger.info("Saving odd: {}", odd);return this.oddRepository.saveAndFlush(odd);
    }

    @Override
    public List<Odd> saveOddList(List<Odd> odds) {
        return this.oddRepository.saveAllAndFlush(odds);
    }

    @Override
    public List<Odd> getALlOdds() {
        return oddRepository.findByBetGroupIdAndFixtureState(1, Constants.FIXTURE_NOT_STARTED);
    }

    @Override
    public List<Odd> getOddsForFixture(Integer fixture) {
        return oddRepository.findByFixtureStateAndFixtureId(Constants.FIXTURE_NOT_STARTED, fixture);
    }

    @Override
    public List<Odd> getOddsForFixtureAndBetGroup(Integer fixtureId, Integer betGroupId) {
        return oddRepository.findByFixtureStateAndFixtureIdAndBetGroupId(Constants.FIXTURE_NOT_STARTED, fixtureId, betGroupId);
    }


    @Override
    public List<OddDTO> createOddDTOList(Integer fixtureId, Integer betGroupId) {

        List<OddDTO> oddDTOList = new ArrayList<>();
        List<Odd> oddList = getOddsForFixtureAndBetGroup(fixtureId, betGroupId);
        for (Odd odd : oddList) {
            try {
                OddDTO oddDTO = oddMapper.oddToOddDTO(odd);
                oddDTOList.add(oddDTO);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return oddDTOList;
    }

    @Override
    public boolean existsWithFixtureIdAndBetGroupId(Integer fixtureId, Integer betGroupId){
        return oddRepository.existsByFixtureIdAndBetGroupId(fixtureId, betGroupId);
    }

    @Override
    public ApiResponse<?> getOddsForFixtureApiResponse(Integer integer) {
        return ApiResponseUtil.transformListToApiResponse(getOddsForFixture(integer), "Odds");
    }

    @Override
    public ApiResponse<?> getALlOddsApiResponse() {
        return ApiResponseUtil.transformListToApiResponse(getALlOdds(), "Odds");
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
