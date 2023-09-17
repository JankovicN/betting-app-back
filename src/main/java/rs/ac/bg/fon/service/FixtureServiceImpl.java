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
import java.time.LocalDateTime;
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
        logger.info("Saving fixture: {}", fixture);
        return fixtureRepository.save(fixture);
    }

    @Override
    public Fixture getFixtureById(int fixtureId) {
        Optional<Fixture> fixture = fixtureRepository.findById(fixtureId);
        if (fixture.isPresent()) {
            logger.info("getFixtureById: Found fixture with id = " + fixtureId);
            return fixture.get();
        }
        logger.warn("getFixtureById: No fixture is found for id = " + fixtureId);
        return new Fixture();
    }

    @Override
    public List<Fixture> getNotStarted() {
        List<Fixture> fixtures = this.fixtureRepository.findByState(Constants.FIXTURE_NOT_STARTED);
        if(fixtures == null || fixtures.isEmpty()){
            logger.warn("getNotStarted: No fixture is found for state = NS");
        }else{
            for (Fixture f : fixtures) {
                Optional<Team> home = teamService.findById(f.getHome().getId());
                Optional<Team> away = teamService.findById(f.getAway().getId());
                f.setHome(home.get());
                f.setAway(away.get());
            }
        }
        return fixtures;
    }

    @Override
    public boolean existsByDate(LocalDateTime date) {
        List<Fixture> fixtures = fixtureRepository.findByDate(java.sql.Timestamp.valueOf(date));
        return fixtures.size() > 0;
    }


    @Override
    public List<Fixture> getNotStartedByLeague(Integer league) {
        List<Fixture> fixtures = this.fixtureRepository.findByStateAndLeagueId(Constants.FIXTURE_NOT_STARTED, league);
        for (Fixture f : fixtures) {
            Optional<Team> home = teamService.findById(f.getHome().getId());
            Optional<Team> away = teamService.findById(f.getAway().getId());
            f.setHome(home.get());
            f.setAway(away.get());
        }
        return fixtures;
    }

    @Override
    public List<FixtureDTO> createFixtureDTOList(List<Fixture> fixtures) {
        List<FixtureDTO> fixtureDTOS = new ArrayList<>();
        for (Fixture fixture : fixtures) {
            List<BetGroupDTO> betGroupDTOList = betGroupService.createBetGroupDTOList(fixture.getId());
            Optional<Team> home = teamService.findById(fixture.getHome().getId());
            Optional<Team> away = teamService.findById(fixture.getAway().getId());

            if (home.isEmpty() || away.isEmpty() || betGroupDTOList.isEmpty()) {
                continue;
            }

            try {
                TeamDTO homeDTO = teamService.createBetGroupDTO(home.get());
                TeamDTO awayDTO = teamService.createBetGroupDTO(away.get());
                FixtureDTO fixtureDTO = fixtureMapper.fixtureToFixtureDTO(fixture, homeDTO, awayDTO, betGroupDTOList);
                fixtureDTOS.add(fixtureDTO);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return fixtureDTOS;
    }

    @Override
    public ApiResponse<?> getNotStartedByLeagueApiCall(Integer league) {
        return ApiResponseUtil.transformListToApiResponse(getNotStartedByLeague(league), "fixtures");
    }

    @Override
    public ApiResponse<?> getNotStartedApiCall() {
        return ApiResponseUtil.transformListToApiResponse(getNotStarted(), "fixtures");
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
