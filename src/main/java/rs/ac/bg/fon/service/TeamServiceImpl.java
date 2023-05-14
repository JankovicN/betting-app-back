package rs.ac.bg.fon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Team;
import rs.ac.bg.fon.repository.TeamRepository;

@Service
public class TeamServiceImpl implements TeamService {

    TeamRepository teamRepository;

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {

        this.teamRepository = teamRepository;
    }

    @Override
    public Team save(Team team) {
        return teamRepository.save(team);
    }
}
