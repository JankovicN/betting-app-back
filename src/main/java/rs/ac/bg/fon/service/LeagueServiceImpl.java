package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
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
public class LeagueServiceImpl implements LeagueService{
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
        logger.info("Saving league: {}", league);
        return leagueRepository.save(league);
    }

    @Override
    public List<League> saveLeagues(List<League> leagues) {
        logger.info("Saving leagues!");
        return leagueRepository.saveAll(leagues);
    }

    @Override
    public List<League> getAllLeagues(){
        logger.info("Getting all leagues!");
        return leagueRepository.findAll();
    }


    @Override
    public boolean exists() {
        return leagueRepository.count()>0;
    }

    @Override
    public ApiResponse<?> getAllLeaguesDTOS() {
        List<LeagueDTO> leagueDTOS = new ArrayList<>();
        List<League> allLeagues = getAllLeagues();

        for (League league : allLeagues) {

            if (league == null ) {
                continue;
            }
            List<Fixture> fixturesList = fixtureService.getNotStartedByLeague(league.getId());
            if ( fixturesList == null || fixturesList.isEmpty()) {
                continue;
            }
            List<FixtureDTO> fixtureDTOS = fixtureService.createFixtureDTOList(fixturesList);
            try{
                LeagueDTO leagueDTO = leagueMapper.leagueToLeagueDTO(league, fixtureDTOS);
                leagueDTOS.add(leagueDTO);
            }catch(Exception e){
                logger.error(e.getMessage());
            }
        }
        return ApiResponseUtil.transformListToApiResponse(leagueDTOS, "leagues");
    }



    @Autowired
    public void setLeagueMapper(LeagueMapper leagueMapper) {
        this.leagueMapper = leagueMapper;
    }
}
