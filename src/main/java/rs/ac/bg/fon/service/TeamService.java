package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Team;

import java.util.Optional;

@Service
public interface TeamService {
    Team save(Team team);

    TeamDTO createBetGroupDTO(Team home);

    Optional<Team> findById(Integer id);
}
