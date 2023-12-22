package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.util.FixtureGenerator;
import rs.ac.bg.fon.BettingAppBack.util.OddGenerator;
import rs.ac.bg.fon.BettingAppBack.util.OddGroupGenerator;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.OddGroup;
import rs.ac.bg.fon.repository.OddRepository;
import rs.ac.bg.fon.service.OddServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OddServiceImplTest {
    @InjectMocks
    private OddServiceImpl oddService;
    @Mock
    private OddRepository oddRepository;


    // Save Odd
    @Test
    void testSaveOddSuccess() {
        Odd odd = OddGenerator.generateUniqueOdd();
        when(oddRepository.saveAndFlush(odd)).thenReturn(odd);

        Odd savedOdd = oddService.save(odd);
        assertEquals(odd, savedOdd);
    }

    @Test
    void testSaveOddShouldReturnNull() {
        when(oddRepository.saveAndFlush(null)).thenThrow(new NullPointerException());

        Odd savedOdd = oddService.save(null);
        assertEquals(null, savedOdd);
    }


    // Save Odd List
    @Test
    void testSaveOddListSuccess() {
        List<Odd> oddList = OddGenerator.generateUniqueOdds(2);
        when(oddRepository.saveAllAndFlush(oddList)).thenReturn(oddList);

        List<Odd> savedOddList = oddService.saveOddList(oddList);
        assertEquals(oddList, savedOddList);
    }

    @Test
    void testSaveOddListShouldReturnEmptyList() {
        when(oddRepository.saveAllAndFlush(null)).thenThrow(new NullPointerException());

        List<Odd> savedOddList = oddService.saveOddList(null);
        assertEquals(new ArrayList<>(), savedOddList);
    }


    // Get Odd with ID
    @Test
    void testGetOddByIdSuccess() {
        Odd odd = OddGenerator.generateUniqueOdd();
        when(oddRepository.findById(odd.getId())).thenReturn(Optional.of(odd));

        Odd foundOdd = oddService.getOddById(odd.getId());
        assertEquals(odd, foundOdd);
    }

    @Test
    void testGetOddByIdDatabaseError() {
        when(oddRepository.findById(anyInt())).thenThrow(new RuntimeException("Simulated database error!"));

        Odd foundOdd = oddService.getOddById(anyInt());
        assertNull(foundOdd);
    }

    @Test
    void testGetOddByIdOddNotFound() {
        when(oddRepository.findById(anyInt())).thenReturn(Optional.empty());

        Odd foundOdd = oddService.getOddById(anyInt());
        assertNull(foundOdd);
    }


    // Get Odds for Fixture and Odd Group
    @Test
    void testGetOddsForFixtureAndOddGroupSuccess() {
        Fixture fixture = FixtureGenerator.generateUniqueFixture();
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddRepository.findByFixtureStateAndFixtureIdAndOddGroupId(eq(Constants.FIXTURE_NOT_STARTED), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(new Odd(), new Odd()));

        List<Odd> oddsForFixtureAndOddGroup = oddService.getOddsForFixtureAndOddGroup(fixture.getId(), oddGroup.getId());

        assertEquals(2, oddsForFixtureAndOddGroup.size());
    }
    @Test
    void testGetOddsForFixtureAndOddGroupDatabaseError() {

        when(oddRepository.findByFixtureStateAndFixtureIdAndOddGroupId(eq(Constants.FIXTURE_NOT_STARTED), anyInt(), anyInt()))
                .thenThrow(new NullPointerException());

        List<Odd> oddsForFixtureAndOddGroup = oddService.getOddsForFixtureAndOddGroup(null, null);

        assertEquals(0, oddsForFixtureAndOddGroup.size());
    }
    @Test
    void testGetOddsForFixtureAndOddGroupNoOddsWereFound() {
        Fixture fixture = FixtureGenerator.generateUniqueFixture();
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddRepository.findByFixtureStateAndFixtureIdAndOddGroupId(eq(Constants.FIXTURE_NOT_STARTED), anyInt(), anyInt()))
                .thenReturn(null);

        List<Odd> oddsForFixtureAndOddGroup = oddService.getOddsForFixtureAndOddGroup(fixture.getId(), oddGroup.getId());

        assertEquals(0, oddsForFixtureAndOddGroup.size());
    }

    // Create Odd DTO List
    @Test
    void testCreateOddDTOListSuccess() {
        Fixture fixture = FixtureGenerator.generateUniqueFixture();
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddRepository.findByFixtureStateAndFixtureIdAndOddGroupId(eq(Constants.FIXTURE_NOT_STARTED), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(OddGenerator.generateUniqueOdd(), OddGenerator.generateUniqueOdd()));

        List<OddDTO> oddDTOList = oddService.createOddDTOList(fixture.getId(), oddGroup.getId());

        assertEquals(2, oddDTOList.size());
    }
    @Test
    void testCreateOddDTOListDatabaseError() {

        when(oddRepository.findByFixtureStateAndFixtureIdAndOddGroupId(eq(Constants.FIXTURE_NOT_STARTED), anyInt(), anyInt()))
                .thenThrow(new NullPointerException());

        List<OddDTO> oddDTOList = oddService.createOddDTOList(null, null);

        assertEquals(0, oddDTOList.size());
    }
    @Test
    void testCreateOddDTOListNoOddsWereFound() {
        Fixture fixture = FixtureGenerator.generateUniqueFixture();
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddRepository.findByFixtureStateAndFixtureIdAndOddGroupId(eq(Constants.FIXTURE_NOT_STARTED), anyInt(), anyInt()))
                .thenReturn(null);

        List<OddDTO> oddDTOList = oddService.createOddDTOList(fixture.getId(), oddGroup.getId());

        assertEquals(0, oddDTOList.size());
    }


    // Do Odds for Fixture and Odd Group exist
    @Test
    void testExistsWithFixtureIdAndOddGroupIdSuccess() {
        Fixture fixture = FixtureGenerator.generateUniqueFixture();
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddRepository.existsByFixtureIdAndOddGroupId(anyInt(), anyInt()))
                .thenReturn(true);

        boolean exists = oddService.existsWithFixtureIdAndOddGroupId(fixture.getId(), oddGroup.getId());

        assertTrue(exists);
    }

    @Test
    void testExistsWithFixtureIdAndOddGroupIdDatabaseError() {
        Fixture fixture = FixtureGenerator.generateUniqueFixture();
        OddGroup oddGroup = OddGroupGenerator.generateUniqueOddGroup();

        when(oddRepository.existsByFixtureIdAndOddGroupId(anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Simulated error"));

        boolean exists = oddService.existsWithFixtureIdAndOddGroupId(fixture.getId(), oddGroup.getId());

        assertFalse(exists);
    }

    @Test
    void testExistsWithFixtureIdAndOddGroupIdNullError() {

        when(oddRepository.existsByFixtureIdAndOddGroupId(anyInt(), anyInt()))
                .thenThrow(new NullPointerException());

        boolean exists = oddService.existsWithFixtureIdAndOddGroupId(null, null);

        assertFalse(exists);
    }
}
