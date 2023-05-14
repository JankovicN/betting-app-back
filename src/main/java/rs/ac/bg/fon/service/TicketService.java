package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.Ticket;

import java.util.List;

public interface TicketService{
    Ticket save(Ticket ticket);

    void updateAllTickets();

    List<Ticket> getUserTickets(String username);

    void proccessTickets();

    void payoutTickets();
}
