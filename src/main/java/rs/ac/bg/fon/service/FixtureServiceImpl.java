package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.BetGroup.BetGroupDTO;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Team;
import rs.ac.bg.fon.mappers.FixtureMapper;
import rs.ac.bg.fon.repository.FixtureRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FixtureServiceImpl implements FixtureService {
    private static final Logger logger = LoggerFactory.getLogger(FixtureServiceImpl.class);

    FixtureRepository fixtureRepository;

    FixtureMapper fixtureMapper;

    BetGroupService betGroupService;
    TeamService teamService;


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

    @Override
    public List<Fixture> getNotStartedByLeague(Integer leagueID) {

        try {
            List<Fixture> fixtures = fixtureRepository.findByStateAndLeagueId(Constants.FIXTURE_NOT_STARTED, leagueID);
            if (fixtures == null || fixtures.isEmpty()) {
                logger.warn("No Fixtures were found for LEAGUE ID = " + leagueID + " and STATE = " + Constants.FIXTURE_NOT_STARTED);
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

    @Override
    public List<FixtureDTO> createFixtureDTOList(List<Fixture> fixtures) {
        try {
            List<FixtureDTO> fixtureDTOS = new ArrayList<>();
            for (Fixture fixture : fixtures) {
                List<BetGroupDTO> betGroupDTOList = betGroupService.createBetGroupDTOList(fixture.getId(), 1);
                Optional<Team> home = teamService.findById(fixture.getHome().getId());
                Optional<Team> away = teamService.findById(fixture.getAway().getId());

                if (home==null || away==null
                        || !home.isPresent()
                        || !away.isPresent()
                        || betGroupDTOList == null
                        || betGroupDTOList.isEmpty()) {
                    continue;
                }

                try {
                    TeamDTO homeDTO = teamService.createTeamDTO(home.get());
                    TeamDTO awayDTO = teamService.createTeamDTO(away.get());
                    FixtureDTO fixtureDTO = fixtureMapper.fixtureToFixtureDTO(fixture, homeDTO, awayDTO, betGroupDTOList);
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

    @Override
    public ApiResponse<?> getNotStartedByLeagueApiCall(Integer league) {
        return ApiResponseUtil.transformListToApiResponse(getNotStartedByLeague(league), "fixtures");
    }

    @Autowired
    public void setBetGroupService(BetGroupService betGroupService) {
        this.betGroupService = betGroupService;
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setFixtureMapper(FixtureMapper fixtureMapper) {
        this.fixtureMapper = fixtureMapper;
    }

    @Autowired
    public void setFixtureRepository(FixtureRepository fixtureRepository) {
        this.fixtureRepository = fixtureRepository;
    }

}
