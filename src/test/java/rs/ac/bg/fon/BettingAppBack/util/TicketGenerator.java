package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketGenerator {
    private static int ticketCounter = 1;

    public static Ticket generateUniqueTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(ticketCounter++);
        ticket.setState(Constants.TICKET_UNPROCESSED);
        ticket.setWager(BigDecimal.valueOf(ticketCounter*20));
        ticket.setTotalWin(BigDecimal.valueOf(ticketCounter*100));
        ticket.setOdd(ticketCounter+1.23);
        ticket.setDate(LocalDateTime.now());
        ticket.setBets(generateBetsForTicket(ticket));
        return ticket;
    }

    public static List<Ticket> generateUniqueTickets(int numOfTickets){
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0;i<numOfTickets;i++){
            tickets.add(generateUniqueTicket());
        }
        return tickets;
    }

    private static List<Bet> generateBetsForTicket(Ticket ticket) {
        List<Bet> bets = BetGenerator.generateUniqueBets(2);
        BetGenerator.setTicketForBets(bets,ticket);
        return bets;
    }
}
