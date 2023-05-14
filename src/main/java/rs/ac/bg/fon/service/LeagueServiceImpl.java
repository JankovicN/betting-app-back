package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.repository.LeagueRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeagueServiceImpl implements LeagueService{
    private static final Logger logger = LoggerFactory.getLogger(LeagueServiceImpl.class);
    LeagueRepository leagueRepository;

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
}
