package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.BetGroup.BetGroupDTO;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.mappers.BetGroupMapper;
import rs.ac.bg.fon.repository.BetGroupRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BetGroupServiceImpl implements BetGroupService {
    private static final Logger logger = LoggerFactory.getLogger(BetGroupServiceImpl.class);
    private BetGroupRepository betGroupRepository;
    private OddService oddService;
    private BetGroupMapper betGroupMapper;

    @Override
    public ApiResponse<?> getAllBetGroupsApiResponse() {
        return ApiResponseUtil.transformListToApiResponse(getAllBetGroups(), "bet groups");
    }

    @Override
    public ApiResponse<?> getBetGroupsByFixtureApiResponse(Integer fixture) {
        return ApiResponseUtil.transformListToApiResponse(getBetGroupsByFixture(fixture), "fixtures");
    }

    @Override
    public BetGroup getBetGroupWithId(Integer betGroupId) {
        Optional<BetGroup> betGroup = betGroupRepository.findById(betGroupId);
        if (betGroup.isPresent()) {
            return betGroup.get();
        }
        return new BetGroup();
    }

    @Override
    public void deleteBetGroup(Integer id) {
        this.betGroupRepository.deleteById(id);
    }

    @Override
    public List<BetGroupDTO> createBetGroupDTOList(Integer fixtureId) {
        List<BetGroupDTO> betGroupDTOList = new ArrayList<>();
        List<BetGroup> betGroupList=getAllBetGroups();
        for (BetGroup betGroup : betGroupList) {
            List<OddDTO> oddList = oddService.createOddDTOList(fixtureId,betGroup.getId());
            try {
                BetGroupDTO betGroupDTO = betGroupMapper.betGroupToBetGroupDTO(betGroup,oddList);
                betGroupDTOList.add(betGroupDTO);
            } catch (Exception e) {
                // TODO add to api response
                //logger.error(e.getMessage());
            }
        }
        return betGroupDTOList;
    }

    @Override
    public ApiResponse<?> deleteBetGroupApiResponse(Integer id) {
        deleteBetGroup(id);
        ApiResponse response = new ApiResponse();
        response.addInfoMessage("Successfully deleted bet group with id = " + id);
        return response;
    }

    @Override
    public List<BetGroup> getBetGroupsByFixture(Integer fixture) {
        return betGroupRepository.findByOddsFixtureId(fixture);
    }


    @Override
    public BetGroup saveBetGroup(BetGroup betGroup) {
        return betGroupRepository.saveAndFlush(betGroup);
    }

    @Override
    public List<BetGroup> saveBetGroups(List<BetGroup> betGroups) {
        return betGroupRepository.saveAllAndFlush(betGroups);
    }

    @Override
    public List<BetGroup> getAllBetGroups() {
        return this.betGroupRepository.findAll();
    }

    @Override
    public long countRows() {
        return betGroupRepository.count();
    }

    @Override
    public boolean exists() {
        return betGroupRepository.count() > 0;
    }

    @Override
    public boolean existsWithId(Integer betGroupId) {
        return betGroupRepository.existsById(betGroupId);
    }


    @Autowired
    public void setOddService(OddService oddService) {
        this.oddService = oddService;
    }

    @Autowired
    public void setBetGroupMapper(BetGroupMapper betGroupMapper) {
        this.betGroupMapper = betGroupMapper;
    }

    @Autowired
    public void setBetGroupRepository(BetGroupRepository betGroupRepository) {
        this.betGroupRepository = betGroupRepository;
    }

}
