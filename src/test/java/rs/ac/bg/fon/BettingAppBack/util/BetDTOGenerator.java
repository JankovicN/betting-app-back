package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Bet.BetDTO;
import rs.ac.bg.fon.entity.Bet;

import java.util.ArrayList;
import java.util.List;

public class BetDTOGenerator {
    private static int betCounter = 1;
    public static BetDTO generateUniqueBetDTO() {
        BetDTO bet = new BetDTO();
        bet.setOddId(betCounter++);
        return bet;
    }

    public static List<BetDTO> generateUniqueBetDTOList(int numOfBets){
        List<BetDTO> bets = new ArrayList<>();
        for (int i = 0;i<numOfBets;i++){
            bets.add(generateUniqueBetDTO());
        }
        return bets;
    }
}
