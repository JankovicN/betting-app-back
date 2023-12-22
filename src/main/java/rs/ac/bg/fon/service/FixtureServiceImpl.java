package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.OddGroup.OddGroupDTO;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Team;
import rs.ac.bg.fon.mappers.FixtureMapper;
import rs.ac.bg.fon.repository.FixtureRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a service layer class responsible for implementing all Fixture related methods.
 *
 * @author Janko
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FixtureServiceImpl implements FixtureService {

    /**
     * Instance of Logger class, responsible for displaying messages that contain information about the success of methods inside Fixture service class.
     */
    private static final Logger logger = LoggerFactory.getLogger(FixtureServiceImpl.class);

    /**
     * Instance of Fixture repository class, responsible for interacting with fixture table in database.
     */
    private final FixtureRepository fixtureRepository;

    /**
     * Instance of Odd Group service class, responsible for executing any logic related to Odd Group entity.
     */
    private final OddGroupService oddGroupService;

    /**
     * Instance of Team service class, responsible for executing any logic related to Team entity.
     */
    private final TeamService teamService;

    /**
     * Adds new fixture to database. Returns instance of saved fixture from database.
     *
     * @param fixture instance of Fixture class that is being saved.
     * @return instance of Fixture class that is saved in database,
     * or null if error occurs.
     */
    @Override
    public Fixture save(Fixture fixture) {
        try {
            Fixture savedFixture = fixtureRepository.save(fixture);
            logger.info("Successfully saved Fixture " + savedFixture + "!");
            return savedFixture;
        } catch (Exception e) {
            logger.error("Error while trying to save Fixture " + fixture + "!\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Return Fixture object for id that is specified.
     *
     * @param fixtureID Integer value representing id of Fixture.
     * @return instance of Fixture class,
     * or null if error occurs or if there is no fixture with specified id.
     */
    @Override
    public Fixture getFixtureById(Integer fixtureID) {
        try {
            Optional<Fixture> fixture = fixtureRepository.findById(fixtureID);

            if (fixture.isPresent()) {
                logger.info("Successfully found Fixture with ID = " + fixtureID + "!");
                return fixture.get();
            }
            logger.warn("No fixture is found for ID = " + fixtureID);
            return null;
        } catch (Exception e) {
            logger.error("Error while trying to find Fixture with ID = " + fixtureID + "!\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Return list of fixtures that have not started and are contained in specified league.
     *
     * @param leagueID Integer value representing id of league which contains fixtures.
     * @return list of Fixture objects that have not started and are contained in league with specified id,
     * or empty list if an error occurs.
     */
    @Override
    public List<Fixture> getNotStartedByLeague(Integer leagueID) {

        try {
            List<Fixture> fixtures = fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(Constants.FIXTURE_NOT_STARTED, leagueID);
            if (fixtures == null || fixtures.isEmpty()) {
                logger.warn("No Fixtures were found for LEAGUE ID = " + leagueID + " and STATE = " + Constants.FIXTURE_NOT_STARTED);
                return new ArrayList<>();
            } else {
                logger.info("Successfully found all Fixtures that have LEAGUE ID = " + leagueID + " and STATE = " + Constants.FIXTURE_NOT_STARTED + "!");
                for (Fixture f : fixtures) {
                    Optional<Team> home = teamService.findById(f.getHome().getId());
                    Optional<Team> away = teamService.findById(f.getAway().getId());
                    f.setHome(home.get());
                    f.setAway(away.get());

                }
            }
            return fixtures;
        } catch (Exception e) {
            logger.error("Error while trying to find Fixtures with LEAGUE ID = " + leagueID + " and STATE = " + Constants.FIXTURE_NOT_STARTED + "!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Return list of fixtures DTOs that are contained in specified league.
     *
     * @param leagueID Integer value representing id of league which contains fixtures.
     * @return list of FixtureDTO objects that are contained in league with specified id,
     * or empty list if an error occurs.
     */
    @Override
    public List<FixtureDTO> getFixtureDtoByLeague(Integer leagueID) {

        try {
            List<Fixture> fixtureList = getNotStartedByLeague(leagueID);
            return createFixtureDTOList(fixtureList);
        } catch (Exception e) {
            logger.error("Error while trying to find Fixture with LEAGUE ID = " + leagueID + " and STATE = " + Constants.FIXTURE_NOT_STARTED + "!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Transforms and returns list of FixtureDTO objects.
     *
     * @param fixtures list of Fixture objects.
     * @return list of FixtureDTO objects that are representing list of Fixture objects,
     * or empty list if an error occurs.
     */
    @Override
    public List<FixtureDTO> createFixtureDTOList(List<Fixture> fixtures) {
        try {
            List<FixtureDTO> fixtureDTOS = new ArrayList<>();
            for (Fixture fixture : fixtures) {
                List<OddGroupDTO> oddGroupDTOList = oddGroupService.createOddGroupDTOList(fixture.getId(), 1);
                Optional<Team> home = teamService.findById(fixture.getHome().getId());
                Optional<Team> away = teamService.findById(fixture.getAway().getId());

                if (home == null || away == null || !home.isPresent() || !away.isPresent() || oddGroupDTOList == null || oddGroupDTOList.isEmpty()) {
                    continue;
                }

                try {
                    TeamDTO homeDTO = teamService.createTeamDTO(home.get());
                    TeamDTO awayDTO = teamService.createTeamDTO(away.get());
                    FixtureDTO fixtureDTO = FixtureMapper.fixtureToFixtureDTO(fixture, homeDTO, awayDTO, oddGroupDTOList);
                    fixtureDTOS.add(fixtureDTO);
                } catch (Exception e) {
                    logger.error("Error while trying to create FixtureDTO for Fixture " + fixture + "!\n" + e.getMessage());
                }
            }
            return fixtureDTOS;
        } catch (Exception e) {
            logger.error("Error while trying to create FixtureDTO list from Fixture list!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Checks if there are any fixtures that have not started for a league with id that is specified.
     *
     * @param leagueId Integer value representing id of league which should be checked.
     * @return boolean value, return true if League contains fixtures that have not started and have valid odds related to them,
     * otherwise return false.
     */
    @Override
    public boolean existFixtureByLeagueId(Integer leagueId) {
        List<Fixture> notStartedByLeague = getNotStartedByLeague(leagueId);
        boolean existsByLeagueID = notStartedByLeague.isEmpty();
        if (existsByLeagueID) {
            return false;
        }
        for (Fixture fixture : notStartedByLeague) {
            if (fixture != null && !fixture.getOdds().isEmpty()) {
                existsByLeagueID = true;
            }
        }

        return existsByLeagueID;
    }


}
