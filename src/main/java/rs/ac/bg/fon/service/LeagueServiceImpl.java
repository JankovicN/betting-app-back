package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
@RequiredArgsConstructor
@Transactional
public class LeagueServiceImpl implements LeagueService {
    private static final Logger logger = LoggerFactory.getLogger(LeagueServiceImpl.class);
    LeagueRepository leagueRepository;

    private LeagueMapper leagueMapper;

    private FixtureService fixtureService;

    @Autowired
    public void setFixtureService(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    @Autowired
    public void setLeagueRepository(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

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

    @Override
    public List<League> saveLeagues(List<League> leagues) {
        try {
            List<League> savedLeagues = leagueRepository.saveAll(leagues);
            logger.info("Successfully saved Leagues!");
            return savedLeagues;
        } catch (Exception e) {
            logger.error("Error while trying to save Leagues !\n" + e.getMessage());
            return null;
        }
    }

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


    @Override
    public boolean exists() {
        return leagueRepository.count() > 0;
    }

    @Override
    public ApiResponse<?> getAllLeaguesWithFixturesApiResponse() {
        List<LeagueDTO> leagueDTOS = new ArrayList<>();
        List<League> allLeagues = getAllLeagues();
        if (allLeagues != null) {
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
                    LeagueDTO leagueDTO = leagueMapper.leagueToLeagueDTO(league, fixtureDTOS);
                    leagueDTOS.add(leagueDTO);
                } catch (Exception e) {
                    logger.error("Error creating LeagueDTO for League " + league + "!", e);
                }
            }
        }
        return ApiResponseUtil.transformListToApiResponse(leagueDTOS, "leagues");
    }

    @Override
    public ApiResponse<?> getAllLeaguesApiResponse() {
        List<LeagueBasicDTO> leagueDTOS = new ArrayList<>();
        List<League> allLeagues = getAllLeagues();
        if (allLeagues != null) {
            try{
                leagueDTOS = leagueMapper.leagueListToLeagueBasicDTOList(allLeagues);
            }catch (Exception e){
                logger.error("Unable to create League DTO List!\n"+e.getMessage());
            }
        }
        return ApiResponseUtil.transformListToApiResponse(leagueDTOS, "leagues");
    }


    @Autowired
    public void setLeagueMapper(LeagueMapper leagueMapper) {
        this.leagueMapper = leagueMapper;
    }
}
