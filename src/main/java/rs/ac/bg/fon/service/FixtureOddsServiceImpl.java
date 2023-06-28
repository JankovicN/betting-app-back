package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.entity.Odd;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FixtureOddsServiceImpl implements FixtureOddsService {
    private LeagueService leagueService;
    private FixtureService fixtureService;
    private BetGroupService betGroupService;
    private OddService oddService;

    @Override
    public List<League> getFixturesBasic() {
        List<League> allLeagues = leagueService.getAllLeagues();
        BetGroup matchWinnerBetGroup = betGroupService.getBetGroupMatchWinner();

        for (League league : allLeagues) {
            List<Fixture> fixtureList = getFixturesWithOddsByLeagueId(league.getId(), matchWinnerBetGroup.getId(), matchWinnerBetGroup.getName());
            league.setFixtures(fixtureList);
        }
        return allLeagues;
    }

    private List<Fixture> getFixturesWithOddsByLeagueId(int leagueId, int matchWinnerBetGroupId, String matchWinnerBetGroupName) {

        List<Fixture> fixtureList = fixtureService.getNotStartedByLeague(leagueId);
        for (Fixture fixture : fixtureList) {

            BetGroup fixtureBetGroup = new BetGroup();
            fixtureBetGroup.setId(matchWinnerBetGroupId);
            fixtureBetGroup.setName(matchWinnerBetGroupName);

            List<Odd> oddList = oddService.getOddsForFixtureAndBetGroup(fixture.getId(), matchWinnerBetGroupId);
            fixtureBetGroup.setOdds(oddList);

            List<BetGroup> betGroupList = new ArrayList<>();
            betGroupList.add(fixtureBetGroup);
            fixture.setBetGroupList(betGroupList);
        }

        return fixtureList;
    }

    @Override
    public List<Fixture> getFixturesWithOddsByLeagueId(int leagueId) {

        List<Fixture> fixtureList = fixtureService.getNotStartedByLeague(leagueId);
        for (Fixture fixture : fixtureList) {

            List<BetGroup> betGroupList = betGroupService.getBetGroupsByFixture(fixture.getId());
            for (BetGroup betGroup : betGroupList) {
                List<Odd> oddList = oddService.getOddsForFixtureAndBetGroup(fixture.getId(), betGroup.getId());
                betGroup.setOdds(oddList);
            }
            fixture.setBetGroupList(betGroupList);
        }

        return fixtureList;
    }

    @Override
    public Fixture getFixtureWithOdds(int fixtureId) {
        Fixture fixture = fixtureService.getFixtureById(fixtureId);
        List<BetGroup> betGroupList = betGroupService.getBetGroupsByFixture(fixtureId);
        for (BetGroup betGroup : betGroupList) {
            List<Odd> oddList = oddService.getOddsForFixtureAndBetGroup(fixtureId, betGroup.getId());
            betGroup.setOdds(oddList);
        }
        fixture.setBetGroupList(betGroupList);
        return fixture;
    }

    @Override
    public League getFixturesBasicFilter(League league) {
        List<Fixture> fixtureList = getFixturesWithOddsByLeagueId(league.getId());
        league.setFixtures(fixtureList);

        return league;
    }

    @Autowired
    public void setLeagueService(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @Autowired
    public void setFixtureService(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    @Autowired
    public void setBetGroupService(BetGroupService betGroupService) {
        this.betGroupService = betGroupService;
    }

    @Autowired
    public void setOddService(OddService oddService) {
        this.oddService = oddService;
    }
}
