package rs.ac.bg.fon.service;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Ticket;

import java.util.List;


public interface BetService {

    Bet save(Bet bet);
    void updateAllBets();

    void saveBetsForTicket(List<Bet> betList, Ticket ticket) throws Exception;
}
