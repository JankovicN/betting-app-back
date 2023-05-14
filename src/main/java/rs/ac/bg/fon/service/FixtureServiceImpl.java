package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Team;
import rs.ac.bg.fon.repository.FixtureRepository;
import rs.ac.bg.fon.repository.TeamRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FixtureServiceImpl implements FixtureService {

    FixtureRepository fixtureRepository;
    TeamRepository teamRepository;

    @Autowired
    public void setFixtureRepository(FixtureRepository fixtureRepository) {
        this.fixtureRepository = fixtureRepository;
    }

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Fixture save(Fixture fixture) {
        log.info("Saving league: {}", fixture);
        return fixtureRepository.save(fixture);
    }

    @Override
    public Fixture getFixtureById(int fixtureId) {
        Optional<Fixture> fixture = fixtureRepository.findById(fixtureId);
        if (fixture.isPresent()) {
            return fixture.get();
        }
        return new Fixture();
    }

    @Override
    public List<Fixture> getNotStarted() {
        List<Fixture> fixtures = this.fixtureRepository.findByState("NS");
        for (Fixture f : fixtures) {
            Optional<Team> home = teamRepository.findById(f.getHome().getId());
            Optional<Team> away = teamRepository.findById(f.getAway().getId());
            f.setHome(home.get());
            f.setAway(away.get());
        }
        return fixtures;
    }

    @Override
    public boolean existsByDate(LocalDateTime date) {
        List<Fixture> fixtures = fixtureRepository.findByDate(java.sql.Timestamp.valueOf(date));
        return fixtures.size() > 0;
    }

    @Override
    public List<Fixture> getNotStartedByLeague(int league) {
        List<Fixture> fixtures = this.fixtureRepository.findByStateAndLeagueId("NS", league);
        for (Fixture f : fixtures) {
            Optional<Team> home = teamRepository.findById(f.getHome().getId());
            Optional<Team> away = teamRepository.findById(f.getAway().getId());
            f.setHome(home.get());
            f.setAway(away.get());
        }
        return fixtures;
    }
}
