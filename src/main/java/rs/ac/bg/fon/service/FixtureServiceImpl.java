package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.OddGroup.OddGroupDTO;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Team;
import rs.ac.bg.fon.mappers.FixtureMapper;
import rs.ac.bg.fon.repository.FixtureRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FixtureServiceImpl implements FixtureService {
    private static final Logger logger = LoggerFactory.getLogger(FixtureServiceImpl.class);
    private final FixtureRepository fixtureRepository;
    private final OddGroupService oddGroupService;
    private final TeamService teamService;


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
            List<Fixture> fixtures = fixtureRepository.findAllByStateAndLeagueIdOrderByDateAsc(Constants.FIXTURE_NOT_STARTED, leagueID);
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
    public List<FixtureDTO> getFixtureDtoByLeague(Integer leagueID) {

        try {
            List<Fixture> fixtureList = getNotStartedByLeague(leagueID);
            return createFixtureDTOList(fixtureList);
        } catch (Exception e) {
            logger.error("Error while trying to find Fixture with LEAGUE ID = " + leagueID + " and STATE = " + Constants.FIXTURE_NOT_STARTED + "!\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<FixtureDTO> createFixtureDTOList(List<Fixture> fixtures) {
        try {
            List<FixtureDTO> fixtureDTOS = new ArrayList<>();
            for (Fixture fixture : fixtures) {
                List<OddGroupDTO> oddGroupDTOList = oddGroupService.createOddGroupDTOList(fixture.getId(), 1);
                Optional<Team> home = teamService.findById(fixture.getHome().getId());
                Optional<Team> away = teamService.findById(fixture.getAway().getId());

                if (home == null || away == null
                        || !home.isPresent()
                        || !away.isPresent()
                        || oddGroupDTOList == null
                        || oddGroupDTOList.isEmpty()) {
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

//    @Override
//    public ApiResponse<?> getNotStartedByLeagueApiCall(Integer leagueID) {
//        return ApiResponseUtil.transformListToApiResponse(createFixtureDTOList(getNotStartedByLeague(leagueID)), "fixtures");
//    }
//
//    @Override
//    public ApiResponse<?> getNotStartedByLeaguesApiCall(List<Integer> leagues) {
//        List<Fixture> fixtureList = new ArrayList<>();
//        for (Integer id : leagues) {
//            List<Fixture> fixturesById = getNotStartedByLeague(id);
//            fixtureList.addAll(fixturesById);
//        }
//        return ApiResponseUtil.transformListToApiResponse(createFixtureDTOList(fixtureList), "fixtures");
//    }

    @Override
    public boolean existFixtureByLeagueId(Integer leagueId) {
        return !getNotStartedByLeague(leagueId).isEmpty();
    }


}
