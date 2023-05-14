package rs.ac.bg.fon.service;
import rs.ac.bg.fon.entity.Bet;


public interface BetService {

    Bet save(Bet bet);
    void upadateAllBets();
}
