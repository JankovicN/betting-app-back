package rs.ac.bg.fon.BettingAppBack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.BettingAppBack.util.TeamGenerator;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Team;
import rs.ac.bg.fon.repository.TeamRepository;
import rs.ac.bg.fon.service.TeamServiceImpl;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {

    @InjectMocks
    private TeamServiceImpl teamService;

    @Mock
    private TeamRepository teamRepository;

    // Test save Team
    @Test
    void testSaveSuccess(){
        Team team = TeamGenerator.generateUniqueTeam();
        when(teamRepository.save(team)).thenReturn(team);

        Team savedTeam = teamService.save(team);

        assertEquals(team, savedTeam);
    }
    @Test
    void testSaveNoTeamName(){
        Team team = new Team(1, null, new ArrayList<>(), new ArrayList<>());

        Team savedTeam = teamService.save(team);

        assertNull(savedTeam);
    }
    @Test
    void testSaveNoTeamIsNull(){
        Team savedTeam = teamService.save(null);

        assertNull(savedTeam);
    }
    @Test
    void testSaveDatabaseError(){
        Team team = TeamGenerator.generateUniqueTeam();
        when(teamRepository.save(team)).thenThrow(new RuntimeException("Simulated database error!"));

        Team savedTeam = teamService.save(team);

        assertNull(savedTeam);
    }

    // Test create TeamDTO object
    @Test
    void createTeamDTOSuccess(){
        Team team = TeamGenerator.generateUniqueTeam();
        TeamDTO dto = new TeamDTO(team.getId(),team.getName());

        TeamDTO teamDTO = teamService.createTeamDTO(team);
        assertEquals(teamDTO.getId(), dto.getId());
        assertEquals(teamDTO.getName(), dto.getName());
        assertEquals(teamDTO.getClass(), dto.getClass());
    }
    @Test
    void createTeamDTONoId(){
        Team team = new Team(null, "name", new ArrayList<>(), new ArrayList<>());

        TeamDTO teamDTO = teamService.createTeamDTO(team);
        assertNull(teamDTO);
    }
    @Test
    void createTeamDTONoName(){
        Team team = new Team(1, null, new ArrayList<>(), new ArrayList<>());

        TeamDTO teamDTO = teamService.createTeamDTO(team);
        assertNull(teamDTO);
    }
    @Test
    void createTeamDTOTeamIsNull(){

        TeamDTO teamDTO = teamService.createTeamDTO(null);
        assertNull(teamDTO);
    }


    // Test find Team by ID
    @Test
    void testFindByIdSuccess(){
        Team team = TeamGenerator.generateUniqueTeam();
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        Optional<Team> foundTeam = teamService.findById(team.getId());

        assertTrue(foundTeam.isPresent());
        assertEquals(team, foundTeam.get());
    }
    @Test
    void testFindByIdNullId(){

        Optional<Team> foundTeam = teamService.findById(null);

        assertNull(foundTeam);
    }
    @Test
    void testFindByIdNoTeamIsFound(){
        when(teamRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<Team> foundTeam = teamService.findById(anyInt());

        assertTrue(foundTeam.isEmpty());
    }
    @Test
    void testFindByIdDatabaseError(){
        Team team = TeamGenerator.generateUniqueTeam();
        when(teamRepository.findById(team.getId())).thenThrow(new RuntimeException("Simulated database error!"));

        Optional<Team> foundTeam = teamService.findById(team.getId());

        assertNull(foundTeam);
    }


}
