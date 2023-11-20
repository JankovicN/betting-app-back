package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.dtos.BetGroup.BetGroupDTO;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.mappers.BetGroupMapper;
import rs.ac.bg.fon.repository.BetGroupRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BetGroupServiceImpl implements BetGroupService {
    private static final Logger logger = LoggerFactory.getLogger(BetGroupServiceImpl.class);
    private final BetGroupRepository betGroupRepository;
    private final OddService oddService;


    @Override
    public ApiResponse<?> getBetGroupsByFixtureApiResponse(Integer fixture) {
        return ApiResponseUtil.transformListToApiResponse(getBetGroupsByFixture(fixture), "fixtures");
    }

    @Override
    public BetGroup getBetGroupWithId(Integer betGroupId) {
        try {
            Optional<BetGroup> betGroup = betGroupRepository.findById(betGroupId);
            if (betGroup.isPresent()) {
                logger.info("Found Bet Group with ID = " + betGroupId + "!");
                return betGroup.get();
            }
            logger.warn("No Bet Group found for ID = " + betGroupId + "!");
            return null;
        } catch (Exception e) {
            logger.error("Error while trying to find Bet Group with ID = " + betGroupId + "!\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteBetGroup(Integer betGroupId) throws Exception {
        try {
            betGroupRepository.deleteById(betGroupId);
            logger.info("Deleting Bet Group with ID = " + betGroupId + "!");
        } catch (Exception e) {
            logger.error("Error while trying to delete Bet Group with ID = " + betGroupId + "!\n" + e.getMessage());
            throw new Exception("Error while trying to delete Bet Group with ID = " + betGroupId + "!");
        }
    }

    @Override
    public List<BetGroupDTO> createBetGroupDTOList(Integer fixtureId) {
        try {
            List<BetGroupDTO> betGroupDTOList = new ArrayList<>();
            List<BetGroup> betGroupList = getAllBetGroups();
            for (BetGroup betGroup : betGroupList) {
                List<OddDTO> oddList = oddService.createOddDTOList(fixtureId, betGroup.getId());
                try {
                    BetGroupDTO betGroupDTO = BetGroupMapper.betGroupToBetGroupDTO(betGroup, oddList);
                    betGroupDTOList.add(betGroupDTO);
                } catch (Exception e) {
                    logger.error("Error adding Bet Group with ID = " + betGroup.getId() + " to list!\n" + e.getMessage());
                }
            }
            return betGroupDTOList;
        } catch (Exception e) {
            logger.error("Error while trying to create Bet Group DTO list!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<BetGroupDTO> createBetGroupDTOList(Integer fixtureID, Integer betGroupID) {
        try {
            List<BetGroupDTO> betGroupDTOList = new ArrayList<>();
            BetGroup betGroup = getBetGroupWithId(1);
            List<OddDTO> oddList = oddService.createOddDTOList(fixtureID, betGroupID);
            try {
                BetGroupDTO betGroupDTO = BetGroupMapper.betGroupToBetGroupDTO(betGroup, oddList);
                betGroupDTOList.add(betGroupDTO);
            } catch (Exception e) {
                logger.error("Error adding Bet Group with ID = " + betGroup.getId() + " to list!\n" + e.getMessage());
            }
            return betGroupDTOList;
        } catch (Exception e) {
            logger.error("Error while trying to create Bet Group DTO list!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public ApiResponse<?> deleteBetGroupApiResponse(Integer id) {
        ApiResponse response = new ApiResponse();
        try {
            deleteBetGroup(id);
            response.addInfoMessage("Successfully deleted bet group with ID = " + id);
        } catch (Exception e) {
            logger.error("Error while trying to delete Bet Group with ID = " + id + "!\n" + e.getMessage());
            response.addErrorMessage("Unable to delete Bet Group with ID = " + id + "!");
        }
        return response;
    }

    @Override
    public List<BetGroupDTO> getBetGroupsByFixture(Integer fixtureID) {
        try {
            List<BetGroup> betGroupList = betGroupRepository.findByOddsFixtureIdOrderByIdAsc(fixtureID);
            betGroupList = betGroupList.stream()
                    .distinct()
                    .collect(Collectors.toList());
            List<BetGroupDTO> betGroupDTOList = new ArrayList<>();
            for (BetGroup betGroup : betGroupList) {
                List<OddDTO> oddDTOList = oddService.createOddDTOList(fixtureID, betGroup.getId());
                if (oddDTOList.isEmpty()) {
                    continue;
                }
                try {
                    BetGroupDTO betGroupDTO = BetGroupMapper.betGroupToBetGroupDTO(betGroup, oddDTOList);
                    betGroupDTOList.add(betGroupDTO);
                } catch (Exception e) {
                    logger.error("Error while trying to create Bet Group " + betGroup + "!\n" + e.getMessage());
                }
            }
            logger.info("Successfully found Bet Groups for Fixture ID = " + fixtureID + "!");
            return betGroupDTOList;
        } catch (Exception e) {
            logger.error("Error while trying to find Bet Groups with Fixture ID = " + fixtureID + "!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional
    @Override
    public BetGroup saveBetGroup(BetGroup betGroup) {
        try {
            BetGroup savedBetGroup = betGroupRepository.saveAndFlush(betGroup);
            logger.info("Successfully saved Bet Group " + savedBetGroup + "!");
            return savedBetGroup;
        } catch (Exception e) {
            logger.error("Error while trying to save Bet Group " + betGroup + "!\n" + e.getMessage());
            return null;
        }
    }

    @Transactional
    @Override
    public List<BetGroup> saveBetGroups(List<BetGroup> betGroups) {
        try {
            List<BetGroup> savedBetGroups = betGroupRepository.saveAllAndFlush(betGroups);
            logger.info("Successfully saved Bet Groups!");
            return savedBetGroups;
        } catch (Exception e) {
            logger.error("Error while trying to save Bet Group!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<BetGroup> getAllBetGroups() {
        try {
            List<BetGroup> savedBetGroups = betGroupRepository.findAll();
            logger.info("Successfully found all Bet Groups!");
            return savedBetGroups;
        } catch (Exception e) {
            logger.error("Error while trying to find all Bet Groups!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean exists() {
        return betGroupRepository.count() > 0;
    }

    @Override
    public boolean existsWithId(Integer betGroupId) {
        try {
            boolean exists = betGroupRepository.existsById(betGroupId);
            logger.info("Successfully checked if Bet Group with ID = " + betGroupId + " exists = " + exists + "!");
            return exists;
        } catch (Exception e) {
            logger.error("Error while checking if Bet Group with ID = " + betGroupId + ", exists!\n" + e.getMessage());
            return false;
        }
    }

}
