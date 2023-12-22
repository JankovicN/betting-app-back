package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Team;
import rs.ac.bg.fon.mappers.TeamMapper;
import rs.ac.bg.fon.repository.TeamRepository;

import java.util.Optional;

/**
 * Represents a service layer class responsible for implementing all Team related methods.
 *
 * @author Janko
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    /**
     * Instance of Logger class, responsible for displaying messages that contain information about the success of methods inside Team service class.
     */
    private static final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);

    /**
     * Instance of Team repository class, responsible for interacting with team table in database.
     */
    private final TeamRepository teamRepository;

    /**
     * Adds new team to database. Returns instance of saved team from database.
     *
     * @param team instance of Team class that is being saved.
     * @return instance of Team class that is saved in database,
     * or null if error occurs.
     */
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

    /**
     * Transforms Team object to TeamDTO object and returns it.
     *
     * @param team instance of Team class that is being transformed to DTO.
     * @return instance of TeamDTO object that is a representation of Team object,
     * or null if an error occurs.
     */
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

    /**
     * Return Optional<Team> object for id that is specified.
     *
     * @param teamID Integer value representing id of Team.
     * @return instance of Optional<Team> class,
     * or null if error occurs or if there is no team with specified id.
     */
    @Override
    public Optional<Team> findById(Integer teamID) {
        try {
            if (teamID == null) {
                return null;
            }
            Optional<Team> team = teamRepository.findById(teamID);
            logger.info("Successfully found Team " + team + "!");
            return team;
        } catch (Exception e) {
            logger.error("Error while trying to find Team with ID = " + teamID + "!\n" + e.getMessage());
            return null;
        }
    }
}
