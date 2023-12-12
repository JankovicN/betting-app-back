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

/**
 * Represents a service layer class responsible for implementing all Odd Group related methods.
 * Available API method implementations: GET, DELETE
 *
 * @author Janko
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class OddGroupServiceImpl implements OddGroupService {

    /**
     * Instance of Logger class, responsible for displaying messages that contain information about the success of methods inside Odd Group service class.
     */
    private static final Logger logger = LoggerFactory.getLogger(OddGroupServiceImpl.class);

    /**
     * Instance of Odd Group repository class, responsible for interacting with odd_group table in database.
     */
    private final OddGroupRepository oddGroupRepository;

    /**
     * Instance of Odd service class, responsible for executing any logic related to Odd entity.
     */
    private final OddService oddService;


    /**
     * Returns response for API call, containing list of odd groups and their odds for fixture.
     *
     * @param fixtureID Integer value representing id of fixture that fetching is based on.
     * @return instance of ApiResponse class, containing list of OddGroupDTO objects.
     *
     */
    @Override
    public ApiResponse<?> getOddGroupsByFixtureApiResponse(Integer fixtureID) {
        return ApiResponseUtil.transformListToApiResponse(getOddGroupsByFixture(fixtureID), "odds");
    }

    /**
     * Return OddGroup object with id that is specified.
     *
     * @param oddGroupId Integer value representing id of Odd Group.
     * @return instance of OddGroup class,
     *         or null if error occurs of if there is no Odd Group with specified id.
     *
     */
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

    /**
     * Deletes OddGroup row from database with id that is specified.
     *
     * @param oddGroupId Integer value representing id of Odd Group to be deleted.
     * @throws Exception if there is and error while executing the delete query.
     */
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

    /**
     * Creates and transforms list of OddGroup objects to list of OddGroupDTO objects.
     *
     * @param fixtureID Integer value of fixture id for which odds are being fetched for.
     * @param oddGroupID Integer value of odd group for which odds are being fetched for.
     * @return list of OddGroupDTO objects that are associated with the given fixture and odd group id,
     *         or empty list if an error occurs.
     *
     */
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

    /**
     * Returns response for API call with messages indicating the success of  Odd Group deletion from database.
     *
     * @param oddGroupID Integer value representing id of Odd Group to be deleted.
     * @return instance of ApiResponse class,
     *         containing messages indicating the success of Odd Group deletion from the database.
     *
     */
    @Override
    public ApiResponse<?> deleteOddGroupApiResponse(Integer oddGroupID) {
        ApiResponse response = new ApiResponse();
        try {
            deleteOddGroup(oddGroupID);
            response.addInfoMessage("Successfully deleted Odd Group with ID = " + oddGroupID);
        } catch (Exception e) {
            logger.error("Error while trying to delete Odd Group with ID = " + oddGroupID + "!\n" + e.getMessage());
            response.addErrorMessage("Unable to delete Odd Group with ID = " + oddGroupID + "!");
        }
        return response;
    }

    /**
     * Return list of OddGroupDTO objects that are associated with provided fixture.
     *
     * @param fixtureID Integer value representing id of fixture that odds are fetched for.
     * @return list of OddGroupDTO objects that are contained in fixture with specified id,
     *         or empty list if an error occurs.
     *
     */
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

    /**
     * Adds new odd group to database. Returns instance of saved odd group from database.
     *
     * @param oddGroup instance of OddGroup class that is being saved.
     * @return instance of OddGroup class that is saved in database,
     *         or null if error occurs.
     *
     */
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

    /**
     * Adds list of odd groups to database. Returns list of saved odd group from database.
     *
     * @param oddGroups list of OddGroup objects are being saved.
     * @return list of OddGroup objects that are saved in database,
     *         or empty list if error occurs.
     *
     */
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

    /**
     * Checks if odd_group table has any rows, in other word if there are any odd groups saved so far.
     *
     * @return boolean value, return true if odd_group table is not empty,
     *         otherwise return false.
     *
     */
    @Override
    public boolean exists() {
        return oddGroupRepository.count() > 0;
    }

    /**
     * Checks if there is an odd group with specified id
     *
     * @param oddGroupId Integer value representing id of odd group that search is based on.
     * @return boolean value, return true if there is an odd group with that id,
     *         otherwise return false.
     *
     */
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
