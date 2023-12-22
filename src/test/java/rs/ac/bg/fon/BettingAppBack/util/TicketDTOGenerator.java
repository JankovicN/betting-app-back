package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Bet.BetDTO;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDTOGenerator {
    private static int ticketCounter = 1;

    public static TicketDTO generateUniqueTicket() {
        TicketDTO ticket = new TicketDTO();
        ticket.setWager(BigDecimal.valueOf(ticketCounter*20));
        ticket.setTotalOdd(ticketCounter+1.23);
        ticket.setTotalWin(BigDecimal.valueOf(ticketCounter*100));
        ticket.setUsername("Username"+ticketCounter);
        ticket.setBets(generateBetsForTicket());
        return ticket;
    }

    public static List<TicketDTO> generateUniqueTickets(int numOfTickets){
        List<TicketDTO> tickets = new ArrayList<>();
        for (int i = 0;i<numOfTickets;i++){
            tickets.add(generateUniqueTicket());
        }
        return tickets;
    }

    private static List<BetDTO> generateBetsForTicket() {
        List<BetDTO> bets = BetDTOGenerator.generateUniqueBetDTOList(2);
        return bets;
    }
}
