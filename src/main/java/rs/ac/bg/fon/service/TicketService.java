package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket save(Ticket ticket);

    void updateAllTickets();

    List<Ticket> getUserTickets(String username);

    void proccessTickets();

    Double getWagerAmoutForUser(String username);

    Double getTotalWinAmountForUser(String username);

    void cancelTicket(int ticketId);

    Optional<Ticket> getTicketById(int ticketId);

    Ticket createTicketFromJsonString(String ticketJson);
}
