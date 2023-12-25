package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Ticket;

import java.util.ArrayList;
import java.util.List;

public class BetGenerator {
    private static int betCounter = 1;

    public static Bet generateUniqueBet() {
        Bet bet = new Bet();
        bet.setId(betCounter++);
        bet.setState(Constants.BET_NOT_FINISHED);
        return bet;
    }

    public static List<Bet> generateUniqueBets(int numOfBets){
        List<Bet> bets = new ArrayList<>();
        for (int i = 0;i<numOfBets;i++){
            bets.add(generateUniqueBet());
        }
        return bets;
    }
    public static List<Bet> setTicketForBets(List<Bet> bets, Ticket ticket){
        for (Bet bet : bets) {
            bet.setTicket(ticket);
        }
        return bets;
    }
    public static List<Bet> generateBetsForOdds(List<Odd> odds){
        List<Bet> bets = new ArrayList<>();
        for (Odd odd : odds) {
            Bet bet = generateUniqueBet();
            bet.setOdd(odd);
            bets.add(bet);
        }
        return bets;
    }

}
