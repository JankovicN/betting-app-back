package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.League.LeagueBasicDTO;
import rs.ac.bg.fon.dtos.League.LeagueDTO;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.mappers.LeagueMapper;
import rs.ac.bg.fon.repository.LeagueRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a service layer class responsible for implementing all League related methods.
 * Available API method implementations: GET
 *
 * @author Janko
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LeagueServiceImpl implements LeagueService {

    /**
     * Instance of Logger class, responsible for displaying messages that contain information about the success of methods inside League service class.
     */
    private static final Logger logger = LoggerFactory.getLogger(LeagueServiceImpl.class);

    /**
     * Instance of League repository class, responsible for interacting with league table in database.
     */
    private final LeagueRepository leagueRepository;

    /**
     * Instance of Fixture service class, responsible for executing any logic related to Fixture entity.
     */
    private final FixtureService fixtureService;

    /**
     * Adds new league to database. Returns instance of saved league from database.
     *
     * @param league instance of League class that is being saved.
     * @return instance of League class that is saved in database,
     * or null if error occurs.
     */
    @Override
    public League save(League league) {
        try {
            League savedLeague = leagueRepository.save(league);
            logger.info("Successfully saved League " + savedLeague + "!");
            return savedLeague;
        } catch (Exception e) {
            logger.error("Error while trying to save League " + league + "!\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Adds list of leagues to database. Returns list of saved leagues from database.
     *
     * @param leagues list of League objects are being saved.
     * @return list of League objects that are saved in database,
     * or null if error occurs.
     */
    @Override
    public List<League> saveLeagues(List<League> leagues) {
        try {
            List<League> savedLeagues = leagueRepository.saveAll(leagues);
            logger.info("Successfully saved Leagues!");
            return savedLeagues;
        } catch (Exception e) {
            logger.error("Error while trying to save Leagues !\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Return list of all leagues that are in database.
     *
     * @return list of League objects that are in database,
     * or empty list if an error occurs.
     */
    @Override
    public List<League> getAllLeagues() {
        try {
            List<League> leagueList = leagueRepository.findAll();
            logger.info("Successfully found all Leagues!");
            return leagueList;
        } catch (Exception e) {
            logger.error("Error while trying to find all Leagues !\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if league table has any rows, in other word if there are any leagues saved so far.
     *
     * @return boolean value, return true if league table is not empty,
     * otherwise return false.
     */
    @Override
    public boolean exists() {
        return leagueRepository.count() > 0;
    }

    /**
     * Returns response for API call, containing list of all leagues, where each league contains fixtures that have not started yet.
     *
     * @return instance of ApiResponse class,
     * containing list of League objects, each one containing fixtures which have not started yet.
     */
    @Override
    public ApiResponse<?> getAllLeaguesWithFixturesApiResponse() {
        List<LeagueDTO> leagueDTOS = new ArrayList<>();
        List<League> allLeagues = getAllLeagues();
        if (allLeagues != null && !allLeagues.isEmpty()) {
            for (League league : allLeagues) {
                if (league == null) {
                    continue;
                }

                List<Fixture> fixturesList = fixtureService.getNotStartedByLeague(league.getId());
                if (fixturesList == null || fixturesList.isEmpty()) {
                    continue;
                }

                List<FixtureDTO> fixtureDTOS = fixtureService.createFixtureDTOList(fixturesList);
                if (fixtureDTOS == null || fixtureDTOS.isEmpty()) {
                    continue;
                }
                try {
                    LeagueDTO leagueDTO = LeagueMapper.leagueToLeagueDTO(league, fixtureDTOS);
                    leagueDTOS.add(leagueDTO);
                } catch (Exception e) {
                    logger.error("Error creating LeagueDTO for League " + league + "!", e);
                }
            }
        }
        return ApiResponseUtil.transformListToApiResponse(leagueDTOS, "leagues");
    }

    /**
     * Returns response for API call, containing list of leagues that have at least one fixture that has not started.
     *
     * @return instance of ApiResponse class,
     * containing list of League objects.
     */
    @Override
    public ApiResponse<?> getAllLeaguesApiResponse() {
        List<LeagueBasicDTO> leagueDTOS = new ArrayList<>();
        List<League> allLeagues = getAllLeagues();
        if (allLeagues != null && !allLeagues.isEmpty()) {
            for (League league : allLeagues) {
                try {
                    if (fixtureService.existFixtureByLeagueId(league.getId())) {
                        leagueDTOS.add(LeagueMapper.leagueToLeagueBasicDTO(league));
                    }
                } catch (Exception e) {
                    logger.error("Unable to create League DTO for League " + league + "!\n" + e.getMessage());
                }
            }

        }
        return ApiResponseUtil.transformListToApiResponse(leagueDTOS, "leagues");
    }


    /**
     * Returns response for API call, containing LeagueDTO object along with list of FixtureDTO objects for that league.
     *
     * @param leagueId Integer value representing id of League that search is based on.
     * @return instance of ApiResponse class containing LeagueDTO object with list of FixtureDTO objects that are associated with that league,
     * if there is an error, then ApiResponse object with error message is returned instead.
     */
    @Override
    public ApiResponse<?> getNotStartedByLeagueApiCall(Integer leagueId) {
        try {
            League league = leagueRepository.findById(leagueId).get();
            LeagueDTO leagueDTO;
            try {
                List<FixtureDTO> fixtures = fixtureService.getFixtureDtoByLeague(leagueId);
                if (fixtures != null && !fixtures.isEmpty()) {
                    leagueDTO = LeagueMapper.leagueToLeagueDTO(league, fixtures);
                } else {
                    ApiResponse<?> response = new ApiResponse<>();
                    response.addErrorMessage("Unable to get Fixtures for League " + league.getName());
                    logger.error("Unable to create League DTO List!");
                    return response;
                }
            } catch (Exception e) {
                ApiResponse<?> response = new ApiResponse<>();
                response.addErrorMessage("Unknown error!");
                logger.error("Unknown error!" + e.getMessage());
                return response;
            }
            return ApiResponseUtil.transformObjectToApiResponse(leagueDTO, "leagues");
        } catch (Exception e) {
            ApiResponse<?> response = new ApiResponse<>();
            response.addErrorMessage("No League is found for id = " + leagueId + "!");
            logger.error("No League is found for ID = " + leagueId + "!");
            return response;
        }
    }
}
