package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dtos.OddGroup.OddGroupDTO;
import rs.ac.bg.fon.entity.OddGroup;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;


public interface OddGroupService {


    ApiResponse<?> getOddGroupsByFixtureApiResponse(Integer fixture);

    OddGroup saveOddGroup(OddGroup oddGroup);

    List<OddGroup> saveOddGroups(List<OddGroup> oddGroups);

    OddGroup getOddGroupWithId(Integer oddGroupId);

    List<OddGroupDTO> getOddGroupsByFixture(Integer fixture);

    boolean exists();

    boolean existsWithId(Integer oddGroupId);

    void deleteOddGroup(Integer id) throws Exception;


    List<OddGroupDTO> createOddGroupDTOList(Integer fixtureID, Integer oddGroupID);

    ApiResponse<?> deleteOddGroupApiResponse(Integer id);
}
