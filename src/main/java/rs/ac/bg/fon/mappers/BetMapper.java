package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.Bet.BetDTO;
import rs.ac.bg.fon.dtos.Bet.BetInfoDTO;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.OddGroup;
import rs.ac.bg.fon.utility.Utility;

import java.util.ArrayList;
import java.util.List;

@Component
public class BetMapper {

    public static Bet betDTOToBet(BetDTO betDTO) throws Exception {
        if (betDTO == null) {
            throw new Exception("Invalid bet!");
        }
        if (betDTO.getOddId() == null) {
            throw new Exception("Bet DTO object has invalid fields [oddID = " + betDTO.getOddId() + "]");
        }
        Odd odd = new Odd();
        odd.setId(betDTO.getOddId());
        Bet bet = new Bet();
        bet.setOdd(odd);
        return bet;
    }

    public static List<Bet> betDTOListToBetList(List<BetDTO> betDtoList) throws Exception {
        if (betDtoList == null || betDtoList.isEmpty()) {
            throw new Exception("There are no bets in this ticket!");
        }
        List<Bet> betList = new ArrayList<>();
        try {
            for (BetDTO betDTO : betDtoList) {
                betList.add(betDTOToBet(betDTO));
            }
        } catch (Exception e) {
            throw new Exception("One of the bet objects is invalid [ " + e.getMessage() + " ]");
        }
        return betList;
    }


    public static BetInfoDTO betToBetInfoDTO(Bet bet, OddGroup oddGroup, Odd odd, Fixture fixture) throws Exception {
        if (bet.getId() == null || bet.getState() == null || bet.getState().isBlank()
                || oddGroup.getName() == null || oddGroup.getName().isBlank()
                || odd.getOdd() == null || odd.getName() == null || odd.getName().isBlank()
                || fixture.getDate() == null || fixture.getHomeGoals() < 0 || fixture.getAwayGoals() < 0
                || fixture.getHome() == null || fixture.getHome().getName() == null || fixture.getHome().getName().isBlank()
                || fixture.getAway() == null || fixture.getAway().getName() == null || fixture.getAway().getName().isBlank()) {
            throw new Exception("Bet DTO object has invalid fields [bet = " + bet + ", oddGroup = " + oddGroup + ", odd = " + odd + ", fixture = " + fixture + " ]");
        }
        BetInfoDTO betDTO = new BetInfoDTO();
        betDTO.setId(bet.getId());
        betDTO.setState(bet.getState());
        betDTO.setOdd(odd.getOdd());
        betDTO.setName(odd.getName());
        betDTO.setOddGroupName(oddGroup.getName());
        betDTO.setFixtureDate(Utility.formatDateTime(fixture.getDate()));
        betDTO.setHome(fixture.getHome().getName());
        betDTO.setAway(fixture.getAway().getName());
        betDTO.setResult(fixture.getHomeGoals() + ":" + fixture.getAwayGoals());
        return betDTO;
    }
}
