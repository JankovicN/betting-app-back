package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Bet.BetInfoDTO;
import rs.ac.bg.fon.entity.Bet;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE bet b \n" +
            "JOIN odd o ON b.odds_id = o.odd_id \n" +
            "JOIN bet_group bg ON bg.bet_group_id = o.bet_group_id \n" +
            "JOIN fixture f ON o.fixture_id = f.fixture_id \n" +
            "SET b.state = \n" +
            "  CASE \n" +
            "    WHEN bg.bet_group_name='Match Winner' AND o.odd_name='Home' AND f.home_goals > f.away_goals OR\n" +
            "    bg.bet_group_name='Match Winner' AND o.odd_name='Draw' AND f.home_goals = f.away_goals OR\n" +
            "    bg.bet_group_name='Match Winner' AND o.odd_name='Away' AND f.home_goals < f.away_goals OR\n" +
            "    bg.bet_group_name='Goals Over/Under' AND SUBSTRING_INDEX(o.odd_name,' ', 1)='Over' AND f.home_goals + f.away_goals > SUBSTRING_INDEX(o.odd_name,' ', -1) OR\n" +
            "    bg.bet_group_name='Goals Over/Under' AND SUBSTRING_INDEX(o.odd_name,' ', 1)='Under' AND f.home_goals + f.away_goals < SUBSTRING_INDEX(o.odd_name,' ', -1) OR\n" +
            "    bg.bet_group_name='Both Teams Score' AND o.odd_name='Yes' AND f.home_goals > 0 AND f.away_goals > 0 OR\n" +
            "    bg.bet_group_name='Both Teams Score' AND o.odd_name='No' AND ( f.home_goals = 0 OR f.away_goals = 0 )OR\n" +
            "    bg.bet_group_name='Exact Score' AND  f.home_goals = SUBSTRING_INDEX(o.odd_name,':', 1) AND f.away_goals = SUBSTRING_INDEX(o.odd_name,':', -1) OR\n" +
            "    bg.bet_group_name='Double Chance' AND o.odd_name='Home/Draw' AND f.home_goals >= f.away_goals OR\n" +
            "    bg.bet_group_name='Double Chance' AND o.odd_name='Home/Away' AND f.home_goals <> f.away_goals OR\n" +
            "    bg.bet_group_name='Double Chance' AND o.odd_name='Draw/Away' AND f.home_goals <= f.away_goals OR\n" +
            "    bg.bet_group_name='Total - Home'  AND SUBSTRING_INDEX(o.odd_name,' ', 1)='Over' AND f.home_goals > SUBSTRING_INDEX(o.odd_name,' ', -1) OR\n" +
            "    bg.bet_group_name='Total - Home'  AND SUBSTRING_INDEX(o.odd_name,' ', 1)='Under' AND f.home_goals < SUBSTRING_INDEX(o.odd_name,' ', -1) OR\n" +
            "    bg.bet_group_name='Total - Away' AND SUBSTRING_INDEX(o.odd_name,' ', 1)='Over' AND f.away_goals > SUBSTRING_INDEX(o.odd_name,' ', -1) OR\n" +
            "    bg.bet_group_name='Total - Away' AND SUBSTRING_INDEX(o.odd_name,' ', 1)='Under' AND f.away_goals < SUBSTRING_INDEX(o.odd_name,' ', -1) OR\n" +
            "    bg.bet_group_name='Odd/Even' AND o.odd_name='Even' AND ( f.home_goals + f.away_goals ) % 2 = 0 OR\n" +
            "    bg.bet_group_name='Odd/Even' AND o.odd_name='Odd' AND ( f.home_goals + f.away_goals  )% 2 = 1 OR\n" +
            "    bg.bet_group_name='Home Team Score a Goal' AND o.odd_name='Yes' AND f.home_goals > 0 OR\n" +
            "    bg.bet_group_name='Home Team Score a Goal' AND o.odd_name='No' AND f.home_goals = 0 OR\n" +
            "    bg.bet_group_name='Away Team Score a Goal' AND o.odd_name='Yes' AND  f.away_goals > 0 OR\n" +
            "    bg.bet_group_name='Away Team Score a Goal' AND o.odd_name='No' AND  f.away_goals = 0 \n" +
            "\tTHEN '"+ Constants.BET_WIN +"' \n" +
            "    ELSE '"+ Constants.BET_LOSS +"' \n" +
            "  END\n" +
            "  WHERE f.state='"+ Constants.FIXTURE_FULL_TIME +"'", nativeQuery = true)
    void updateAllBets();

    List<Bet> findAllByTicketId(Integer ticketId);
}
