package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.BetGroup.BetGroupDTO;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.entity.Ticket;

import java.math.BigDecimal;

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
}
