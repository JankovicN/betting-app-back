package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dtos.BetGroup.BetGroupDTO;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;


public interface BetGroupService {
    ApiResponse<?> getAllBetGroupsApiResponse();
    ApiResponse<?> getBetGroupsByFixtureApiResponse(Integer fixture);

    BetGroup saveBetGroup(BetGroup betGroup);

    List<BetGroup> saveBetGroups(List<BetGroup> betGroups);

    List<BetGroup> getAllBetGroups();

    BetGroup getBetGroupWithId(Integer betGroupId);
    List<BetGroup> getBetGroupsByFixture(Integer fixture);

    long countRows();

    boolean exists();

    boolean existsWithId(Integer betGroupId);

    void deleteBetGroup(Integer id) throws Exception;

    List<BetGroupDTO> createBetGroupDTOList(Integer fixtureId);

    ApiResponse<?> deleteBetGroupApiResponse(Integer id);
}
