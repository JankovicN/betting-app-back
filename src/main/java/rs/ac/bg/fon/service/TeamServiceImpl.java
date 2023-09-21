package rs.ac.bg.fon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Team;
import rs.ac.bg.fon.mappers.TeamMapper;
import rs.ac.bg.fon.repository.TeamRepository;

import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    private static final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
    TeamRepository teamRepository;
    TeamMapper teamMapper;

    @Transactional
    @Override
    public Team save(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public TeamDTO createBetGroupDTO(Team team) {
        try {
            return teamMapper.teamToTeamDTO(team);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Team> findById(Integer id) {
        return teamRepository.findById(id);
    }

    @Autowired
    public void setTeamMapper(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {

        this.teamRepository = teamRepository;
    }
}
