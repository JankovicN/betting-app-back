package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Team;

import java.util.Optional;

/**
 * Represents a service layer interface responsible for defining all Team related methods.
 *
 * @author Janko
 * @version 1.0
 */
@Service
public interface TeamService {

    /**
     * Adds new team to database. Returns instance of saved team from database.
     *
     * @param team instance of Team class that is being saved.
     * @return instance of Team class that is saved in database,
     * or null if error occurs.
     */
    Team save(Team team);

    /**
     * Transforms Team object to TeamDTO object and returns it.
     *
     * @param team instance of Team class that is being transformed to DTO.
     * @return instance of TeamDTO object that is a representation of Team object,
     * or null if an error occurs.
     */
    TeamDTO createTeamDTO(Team team);

    /**
     * Return Optional<Team> object for id that is specified.
     *
     * @param teamID Integer value representing id of Team.
     * @return instance of Optional<Team> class,
     * or null if error occurs or if there is no team with specified id.
     */
    Optional<Team> findById(Integer teamID);
}
