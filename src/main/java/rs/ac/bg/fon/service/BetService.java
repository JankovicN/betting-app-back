package rs.ac.bg.fon.service;
import rs.ac.bg.fon.dtos.Bet.BetInfoDTO;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;


public interface BetService {

    Bet save(Bet bet);
    void updateAllBets() throws Exception;
    void saveBetsForTicket(List<Bet> betList, Ticket ticket) throws Exception;

    ApiResponse<?> getBetsForTicketApiResponse(Integer ticketId);
}
