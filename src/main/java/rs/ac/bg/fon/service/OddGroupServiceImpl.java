package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.dtos.OddGroup.OddGroupDTO;
import rs.ac.bg.fon.entity.OddGroup;
import rs.ac.bg.fon.mappers.OddGroupMapper;
import rs.ac.bg.fon.repository.OddGroupRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OddGroupServiceImpl implements OddGroupService {
    private static final Logger logger = LoggerFactory.getLogger(OddGroupServiceImpl.class);
    private final OddGroupRepository oddGroupRepository;
    private final OddService oddService;


    @Override
    public ApiResponse<?> getOddGroupsByFixtureApiResponse(Integer fixture) {
        return ApiResponseUtil.transformListToApiResponse(getOddGroupsByFixture(fixture), "odds");
    }

    @Override
    public OddGroup getOddGroupWithId(Integer oddGroupId) {
        try {
            Optional<OddGroup> oddGroup = oddGroupRepository.findById(oddGroupId);
            if (oddGroup.isPresent()) {
                logger.info("Found Odd Group with ID = " + oddGroupId + "!");
                return oddGroup.get();
            }
            logger.warn("No Odd Group found for ID = " + oddGroupId + "!");
            return null;
        } catch (Exception e) {
            logger.error("Error while trying to find Odd Group with ID = " + oddGroupId + "!\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteOddGroup(Integer oddGroupId) throws Exception {
        try {
            oddGroupRepository.deleteById(oddGroupId);
            logger.info("Deleting Odd Group with ID = " + oddGroupId + "!");
        } catch (Exception e) {
            logger.error("Error while trying to delete Odd Group with ID = " + oddGroupId + "!\n" + e.getMessage());
            throw new Exception("Error while trying to delete Odd Group with ID = " + oddGroupId + "!");
        }
    }

    @Override
    public List<OddGroupDTO> createOddGroupDTOList(Integer fixtureID, Integer oddGroupID) {
        try {
            List<OddGroupDTO> oddGroupDTOList = new ArrayList<>();
            OddGroup oddGroup = getOddGroupWithId(oddGroupID);
            List<OddDTO> oddList = oddService.createOddDTOList(fixtureID, oddGroupID);
            try {
                OddGroupDTO oddGroupDTO = OddGroupMapper.oddGroupToOddGroupDTO(oddGroup, oddList);
                oddGroupDTOList.add(oddGroupDTO);
            } catch (Exception e) {
                logger.error("Error adding Odd Group with ID = " + oddGroup.getId() + " to list!\n" + e.getMessage());
            }
            return oddGroupDTOList;
        } catch (Exception e) {
            logger.error("Error while trying to create Odd Group DTO list!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public ApiResponse<?> deleteOddGroupApiResponse(Integer id) {
        ApiResponse response = new ApiResponse();
        try {
            deleteOddGroup(id);
            response.addInfoMessage("Successfully deleted Odd Group with ID = " + id);
        } catch (Exception e) {
            logger.error("Error while trying to delete Odd Group with ID = " + id + "!\n" + e.getMessage());
            response.addErrorMessage("Unable to delete Odd Group with ID = " + id + "!");
        }
        return response;
    }

    @Override
    public List<OddGroupDTO> getOddGroupsByFixture(Integer fixtureID) {
        try {
            List<OddGroup> oddGroupList = oddGroupRepository.findByOddsFixtureIdOrderByIdAsc(fixtureID);
            if (oddGroupList == null || oddGroupList.isEmpty()) {
                logger.error("No Odd Groups were found for Fixture with ID = " + fixtureID);
                return new ArrayList<>();
            }
            oddGroupList = oddGroupList.stream()
                    .distinct()
                    .collect(Collectors.toList());

            List<OddGroupDTO> oddGroupDTOList = new ArrayList<>();
            for (OddGroup oddGroup : oddGroupList) {

                List<OddDTO> oddDTOList = oddService.createOddDTOList(fixtureID, oddGroup.getId());
                if (oddDTOList.isEmpty()) {
                    logger.error("List of odds for Odd Group with ID " + oddGroup.getId() + " and Fixture with ID " + fixtureID + " is empty!");
                    continue;
                }

                try {
                    OddGroupDTO oddGroupDTO = OddGroupMapper.oddGroupToOddGroupDTO(oddGroup, oddDTOList);
                    oddGroupDTOList.add(oddGroupDTO);
                } catch (Exception e) {
                    logger.error("Error while trying to create Odd Group " + oddGroup + "!\n" + e.getMessage());
                }
            }
            logger.info("Successfully found Odd Groups for Fixture ID = " + fixtureID + "!");
            return oddGroupDTOList;
        } catch (Exception e) {
            logger.error("Error while trying to find Odd Groups with Fixture ID = " + fixtureID + "!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional
    @Override
    public OddGroup saveOddGroup(OddGroup oddGroup) {
        try {
            OddGroup savedOddGroup = oddGroupRepository.saveAndFlush(oddGroup);
            logger.info("Successfully saved Odd Group " + savedOddGroup + "!");
            return savedOddGroup;
        } catch (Exception e) {
            logger.error("Error while trying to save Odd Group " + oddGroup + "!\n" + e.getMessage());
            return null;
        }
    }

    @Transactional
    @Override
    public List<OddGroup> saveOddGroups(List<OddGroup> oddGroups) {
        try {
            List<OddGroup> savedOddGroups = oddGroupRepository.saveAllAndFlush(oddGroups);
            logger.info("Successfully saved Odd Groups!");
            return savedOddGroups;
        } catch (Exception e) {
            logger.error("Error while trying to save Odd Group!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean exists() {
        return oddGroupRepository.count() > 0;
    }

    @Override
    public boolean existsWithId(Integer oddGroupId) {
        try {
            boolean exists = oddGroupRepository.existsById(oddGroupId);
            logger.info("Successfully checked if Odd Group with ID = " + oddGroupId + " exists = " + exists + "!");
            return exists;
        } catch (Exception e) {
            logger.error("Error while checking if Odd Group with ID = " + oddGroupId + ", exists!\n" + e.getMessage());
            return false;
        }
    }

}
