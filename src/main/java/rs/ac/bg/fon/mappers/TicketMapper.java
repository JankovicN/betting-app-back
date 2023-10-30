package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Ticket.TicketBasicDTO;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.utility.Utility;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TicketMapper {
    public Ticket ticketDTOToTicket(TicketDTO ticketDTO) throws Exception {
        if (ticketDTO.getWager() == null || ticketDTO.getWager().compareTo(BigDecimal.valueOf(20.0)) < 0
                || ticketDTO.getTotalWin() == null || ticketDTO.getTotalWin().compareTo(BigDecimal.ZERO) < 0
                || ticketDTO.getTotalOdd() == null || ticketDTO.getTotalOdd() < 1.0
                || ticketDTO.getUsername() == null || ticketDTO.getUsername().isBlank()) {
            throw new Exception("TicketDTO object has invalid fields [wager = " + ticketDTO.getWager()
                    + ", totalWin = " + ticketDTO.getTotalWin() + ", totalOdds = " + ticketDTO.getTotalOdd()
                    + ", username = " + ticketDTO.getUsername() + "]");
        }
        Ticket ticket = new Ticket();
        ticket.setWager(ticketDTO.getWager());
        ticket.setTotalWin(ticketDTO.getTotalWin());
        ticket.setOdd(ticketDTO.getTotalOdd());

        return ticket;
    }

    public TicketBasicDTO ticketToTicketBasicDTO(Ticket ticket) throws Exception {
        if (ticket.getId() == null || ticket.getWager() == null
                || ticket.getTotalWin() == null || ticket.getTotalWin().compareTo(BigDecimal.ZERO) < 0
                || ticket.getOdd() > 1.0
                || ticket.getState() == null || ticket.getState().isBlank()
                || ticket.getDate() == null) {
            throw new Exception("Ticket object has invalid fields [ id = " + ticket.getId() + "wager = " + ticket.getWager()
                    + ", totalWin = " + ticket.getTotalWin() + ", totalOdds = " + ticket.getOdd()
                    + ", state = " + ticket.getState() + ", dateOfPay = " + ticket.getDate() + "]");
        }
        String state = ticket.getState();
        switch (state) {
            case Constants.TICKET_UNPROCESSED:
            case Constants.TICKET_PROCESSED:
                state = Constants.WAITING_FOR_RESULTS;
                break;
            case Constants.TICKET_PAYOUT:
                state = Constants.TICKET_WIN;
                break;
        }
        ;

        TicketBasicDTO ticketDTO = new TicketBasicDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setWager(ticket.getWager());
        ticketDTO.setTotalWin(ticket.getTotalWin());
        ticketDTO.setTotalOdd(ticket.getOdd());
        ticketDTO.setState(state);
        ticketDTO.setDateOfPlay(Utility.formatDateTime(ticket.getDate()));

        return ticketDTO;
    }

    public Ticket ticketDTOToTicket(TicketDTO ticketDTO, List<Bet> bets, User user) throws Exception {
        Ticket ticket = ticketDTOToTicket(ticketDTO);
        ticket.setBets(bets);
        ticket.setUser(user);

        return ticket;
    }
}
