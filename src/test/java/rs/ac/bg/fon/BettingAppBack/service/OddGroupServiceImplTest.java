package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.util.BetGenerator;
import rs.ac.bg.fon.BettingAppBack.util.OddGroupGenerator;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.dtos.OddGroup.OddGroupDTO;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.OddGroup;
import rs.ac.bg.fon.repository.OddGroupRepository;
import rs.ac.bg.fon.service.OddGroupServiceImpl;
import rs.ac.bg.fon.service.OddService;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OddGroupServiceImplTest {
    @InjectMocks
    private OddGroupServiceImpl oddGroupService;
    @Mock
    private OddGroupRepository oddGroupRepository;
    @Mock
    private OddService oddService;

    // Get Odd Group with ID
    @Test
    void testGetOddGroupWithIdSuccess() {
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddGroupRepository.findById(oddGroup.getId())).thenReturn(Optional.of(oddGroup));

        OddGroup foundOddGroup = oddGroupService.getOddGroupWithId(oddGroup.getId());
        assertEquals(oddGroup, foundOddGroup);
    }

    @Test
    void testGetOddGroupWithIdDoesNotExist() {

        when(oddGroupRepository.findById(anyInt())).thenReturn(Optional.empty());

        OddGroup foundOddGroup = oddGroupService.getOddGroupWithId(anyInt());
        assertEquals(null, foundOddGroup);
    }

    @Test
    void testGetOddGroupWithIdDatabaseError() {

        when(oddGroupRepository.findById(anyInt())).thenThrow(new NullPointerException());

        OddGroup foundOddGroup = oddGroupService.getOddGroupWithId(anyInt());
        assertEquals(null, foundOddGroup);
    }


    // Delete Odd Group
    @Test
    void testDeleteOddGroupSuccess() throws Exception {
        int oddGroup = 1;
        doNothing().when(oddGroupRepository).deleteById(oddGroup);
        oddGroupService.deleteOddGroup(oddGroup);
        verify(oddGroupRepository, times(1)).deleteById(oddGroup);
    }

    @Test
    public void testDeleteOddGroupInvalidId() {
        Integer oddGroupId = -1;
        doThrow(new RuntimeException("Simulated repository exception, invalid id!")).when(oddGroupRepository).deleteById(oddGroupId);

        Exception exception = assertThrows(Exception.class, () -> {
            oddGroupService.deleteOddGroup(oddGroupId);
        });

        assertEquals("Error while trying to delete Odd Group with ID = " + oddGroupId + "!", exception.getMessage());
    }

    @Test
    public void testDeleteOddGroupIdIsNull() {
        doThrow(new NullPointerException("Simulated repository exception, id is null!")).when(oddGroupRepository).deleteById(null);

        Exception exception = assertThrows(Exception.class, () -> {
            oddGroupService.deleteOddGroup(null);
        });

        assertEquals("Error while trying to delete Odd Group with ID = " + null + "!", exception.getMessage());
    }


    // Create Odd Group DTO list
    @Test
    void testCreateOddGroupDTOListSuccess() {
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddGroupRepository.findById(oddGroup.getId())).thenReturn(Optional.of(oddGroup));

        when(oddService.createOddDTOList(anyInt(), eq(oddGroup.getId())))
                .thenReturn(Arrays.asList(new OddDTO(), new OddDTO()));

        List<OddGroupDTO> oddGroupDTOList = oddGroupService.createOddGroupDTOList(anyInt(), oddGroup.getId());
        assertEquals(1, oddGroupDTOList.size());
        assertEquals(2, oddGroupDTOList.get(0).getOdds().size());
    }

    @Test
    void testCreateOddGroupDTOListOddGroupMapperException() {
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddGroupRepository.findById(oddGroup.getId())).thenReturn(Optional.of(oddGroup));

        when(oddService.createOddDTOList(anyInt(), eq(oddGroup.getId()))).thenReturn(null);

        List<OddGroupDTO> oddGroupDTOList = oddGroupService.createOddGroupDTOList(anyInt(), oddGroup.getId());

        assertEquals(new ArrayList<>(), oddGroupDTOList);
        assertEquals(0, oddGroupDTOList.size());
    }

    @Test
    void testCreateOddGroupDTOListDatabaseError() {

        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddGroupRepository.findById(oddGroup.getId())).thenReturn(Optional.of(oddGroup));

        when(oddService.createOddDTOList(anyInt(), anyInt())).thenThrow(new NullPointerException());

        List<OddGroupDTO> oddGroupDTOList = oddGroupService.createOddGroupDTOList(anyInt(), oddGroup.getId());

        assertEquals(new ArrayList<>(), oddGroupDTOList);
        assertEquals(0, oddGroupDTOList.size());
    }


    // Get Odd Groups for Fixture
    @Test
    void testGetOddGroupByFixtureSuccess() {

        OddGroup oddGroup1=OddGroupGenerator.generateUniqueOddGroup();
        OddGroup oddGroup2=OddGroupGenerator.generateUniqueOddGroup();

        when(oddGroupRepository.findByOddsFixtureIdOrderByIdAsc(anyInt())).thenReturn(Arrays.asList(oddGroup1, oddGroup2));
        when(oddService.createOddDTOList(anyInt(), anyInt())).thenReturn(Arrays.asList(new OddDTO(), new OddDTO()));

        List<OddGroupDTO> oddGroupsByFixture = oddGroupService.getOddGroupsByFixture(anyInt());

        assertEquals(2, oddGroupsByFixture.size());
    }

    @Test
    void testGetOddGroupByFixtureNoOddsForOddGroupAndFixture() {

        OddGroup oddGroup1=OddGroupGenerator.generateUniqueOddGroup();
        OddGroup oddGroup2=OddGroupGenerator.generateUniqueOddGroup();

        when(oddGroupRepository.findByOddsFixtureIdOrderByIdAsc(anyInt())).thenReturn(Arrays.asList(oddGroup1, oddGroup2));
        when(oddService.createOddDTOList(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        List<OddGroupDTO> oddGroupsByFixture = oddGroupService.getOddGroupsByFixture(anyInt());

        assertEquals(0, oddGroupsByFixture.size());
    }

    @Test
    void testGetOddGroupByFixtureNoOddGroupsForFixture() {

        when(oddGroupRepository.findByOddsFixtureIdOrderByIdAsc(anyInt())).thenReturn(new ArrayList<>());

        List<OddGroupDTO> oddGroupsByFixture = oddGroupService.getOddGroupsByFixture(anyInt());

        assertEquals(0, oddGroupsByFixture.size());
    }

    @Test
    void testGetOddGroupByFixtureDatabaseError() {

        when(oddGroupRepository.findByOddsFixtureIdOrderByIdAsc(anyInt())).thenThrow(new NullPointerException());

        List<OddGroupDTO> oddGroupsByFixture = oddGroupService.getOddGroupsByFixture(anyInt());

        assertEquals(0, oddGroupsByFixture.size());
        verify(oddService, times(0)).createOddDTOList(anyInt(),anyInt());
    }


    // Save Odd Group
    @Test
    void testSaveOddGroupSuccess() {
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();
        when(oddGroupRepository.saveAndFlush(oddGroup)).thenReturn(oddGroup);

        OddGroup savedOddGroup = oddGroupService.saveOddGroup(oddGroup);
        assertEquals(oddGroup, savedOddGroup);
    }

    @Test
    void testSaveOddGroupShouldReturnNull() {
        when(oddGroupRepository.saveAndFlush(null)).thenThrow(new NullPointerException());

        OddGroup savedOddGroup = oddGroupService.saveOddGroup(null);
        assertEquals(null, savedOddGroup);
    }


    // Save Odd Group List
    @Test
    void testSaveOddGroupsSuccess() {

        List<OddGroup> oddGroups = OddGroupGenerator.generateUniqueOddGroups(2);
        when(oddGroupRepository.saveAllAndFlush(oddGroups)).thenReturn(oddGroups);

        List<OddGroup> savedOddGroups = oddGroupService.saveOddGroups(oddGroups);
        assertEquals(oddGroups, savedOddGroups);
        assertEquals(oddGroups.size(), savedOddGroups.size());
    }

    @Test
    void testSaveOddGroupsShouldReturnEmptyList() {
        when(oddGroupRepository.saveAllAndFlush(null)).thenThrow(new NullPointerException());

        List<OddGroup> savedOddGroups = oddGroupService.saveOddGroups(null);
        assertEquals(new ArrayList<>(), savedOddGroups);
    }


    // Do any Odd Groups exist in database
    @Test
    void testExistsTrue() {
        when(oddGroupRepository.count()).thenReturn(1L);

        boolean exists = oddGroupService.exists();
        assertTrue(exists);
    }

    @Test
    void testExistsFalse() {
        when(oddGroupRepository.count()).thenReturn(0L);

        boolean exists = oddGroupService.exists();
        assertFalse(exists);
    }


    // Does Odd Group with ID exist
    @Test
    void testExistsWithIdTrue() {
        when(oddGroupRepository.existsById(anyInt())).thenReturn(true);

        boolean exists = oddGroupService.existsWithId(anyInt());
        assertTrue(exists);
    }

    @Test
    void testExistsWithIdFalse() {
        when(oddGroupRepository.existsById(anyInt())).thenReturn(false);

        boolean exists = oddGroupService.existsWithId(anyInt());
        assertFalse(exists);
    }

    @Test
    void testExistsWithError() {
        when(oddGroupRepository.existsById(anyInt())).thenThrow(new RuntimeException("Simulated error!"));

        boolean exists = oddGroupService.existsWithId(anyInt());
        assertFalse(exists);
    }


    // Delete Odd Group API Response
    @Test
    void testDeleteOddGroupApiResponseSuccess(){
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        doNothing().when(oddGroupRepository).deleteById(anyInt());

        ApiResponse<?> apiResponse = oddGroupService.deleteOddGroupApiResponse(oddGroup.getId());

        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully deleted Odd Group with ID = " + oddGroup.getId(), apiResponse.getInfoMessages().get(0));
    }

    @Test
    public void testDeleteOddGroupApiResponseInvalidId() {
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        doThrow(new RuntimeException("Simulated repository exception, invalid id!")).when(oddGroupRepository).deleteById(anyInt());

        ApiResponse<?> apiResponse = oddGroupService.deleteOddGroupApiResponse(oddGroup.getId());

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Unable to delete Odd Group with ID = " + oddGroup.getId() + "!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    public void testDeleteOddGroupApiResponseIdIsNull() {

        doThrow(new NullPointerException("Simulated repository exception, id is null!")).when(oddGroupRepository).deleteById(null);

        ApiResponse<?> apiResponse = oddGroupService.deleteOddGroupApiResponse(null);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Unable to delete Odd Group with ID = " + null + "!", apiResponse.getErrorMessages().get(0));
    }


    // Get Odd Groups for Fixture API Response

    @Test
    void testGetOddGroupByFixtureApiResponseSuccess() {

        OddGroup oddGroup1=OddGroupGenerator.generateUniqueOddGroup();
        OddGroup oddGroup2=OddGroupGenerator.generateUniqueOddGroup();

        when(oddGroupRepository.findByOddsFixtureIdOrderByIdAsc(anyInt())).thenReturn(Arrays.asList(oddGroup1, oddGroup2));
        when(oddService.createOddDTOList(anyInt(), anyInt())).thenReturn(Arrays.asList(new OddDTO(), new OddDTO()));

        ApiResponse<?> oddGroupsByFixtureApiResponse = oddGroupService.getOddGroupsByFixtureApiResponse(anyInt());

        assertEquals(2 ,((List<OddGroupDTO>)oddGroupsByFixtureApiResponse.getData()).size());
        assertTrue(oddGroupsByFixtureApiResponse.getInfoMessages().isEmpty());
        assertTrue(oddGroupsByFixtureApiResponse.getErrorMessages().isEmpty());
    }

    @Test
    void testGetOddGroupByFixtureApiResponseNoOddsForOddGroupAndFixture() {

        OddGroup oddGroup1=OddGroupGenerator.generateUniqueOddGroup();
        OddGroup oddGroup2=OddGroupGenerator.generateUniqueOddGroup();

        when(oddGroupRepository.findByOddsFixtureIdOrderByIdAsc(anyInt())).thenReturn(Arrays.asList(oddGroup1, oddGroup2));
        when(oddService.createOddDTOList(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        ApiResponse<?> oddGroupsByFixtureApiResponse = oddGroupService.getOddGroupsByFixtureApiResponse(anyInt());

        assertEquals(new ArrayList<>(), oddGroupsByFixtureApiResponse.getData());
        assertEquals(1, oddGroupsByFixtureApiResponse.getInfoMessages().size());
        assertEquals("There are no odds to show!", oddGroupsByFixtureApiResponse.getInfoMessages().get(0));
        assertTrue(oddGroupsByFixtureApiResponse.getErrorMessages().isEmpty());
    }

    @Test
    void testGetOddGroupByFixtureApiResponseNoOddGroupsForFixture() {

        when(oddGroupRepository.findByOddsFixtureIdOrderByIdAsc(anyInt())).thenReturn(new ArrayList<>());

        ApiResponse<?> oddGroupsByFixtureApiResponse = oddGroupService.getOddGroupsByFixtureApiResponse(anyInt());

        assertEquals(new ArrayList<>(), oddGroupsByFixtureApiResponse.getData());
        assertEquals(1, oddGroupsByFixtureApiResponse.getInfoMessages().size());
        assertEquals("There are no odds to show!", oddGroupsByFixtureApiResponse.getInfoMessages().get(0));
        assertTrue(oddGroupsByFixtureApiResponse.getErrorMessages().isEmpty());
    }
}
