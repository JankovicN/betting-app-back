package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.Bet.BetDTO;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Team;

import java.util.ArrayList;
import java.util.List;

@Component
public class BetMapper {

    public Bet betDTOToBet(BetDTO betDTO) throws Exception {
        if(betDTO==null){
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

    public List<Bet> betDTOListToBetList(List<BetDTO> betDtoList) throws Exception {
        if(betDtoList==null || betDtoList.isEmpty()){
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
}
