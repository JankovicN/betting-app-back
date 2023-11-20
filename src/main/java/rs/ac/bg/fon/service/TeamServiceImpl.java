package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {
    private static final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
    private final TeamRepository teamRepository;

    @Transactional
    @Override
    public Team save(Team team) {
        try {
            if (team == null || team.getName() == null) {
                return null;
            }
            Team savedTeam = teamRepository.save(team);
            logger.info("Successfully saved Team " + savedTeam + "!");
            return savedTeam;
        } catch (Exception e) {
            logger.error("Error while trying to save Team " + team + "!\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public TeamDTO createTeamDTO(Team team) {
        try {
            if (team == null || team.getName() == null) {
                return null;
            }
            TeamDTO teamDTO = TeamMapper.teamToTeamDTO(team);
            logger.info("Successfully created TeamDTO from Team " + team + "!");
            return teamDTO;
        } catch (Exception e) {
            logger.error("Error while trying to create TeamDTO from Team " + team + "!\n" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Optional<Team> findById(Integer id) {
        try {
            if (id == null) {
                return null;
            }
            Optional<Team> team = teamRepository.findById(id);
            logger.info("Successfully found Team " + team + "!");
            return team;
        } catch (Exception e) {
            logger.error("Error while trying to find Team with ID = " + id + "!\n" + e.getMessage());
            return null;
        }
    }
}
