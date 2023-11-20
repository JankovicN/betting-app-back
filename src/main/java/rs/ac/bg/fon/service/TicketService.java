package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;

public interface TicketService {
    Ticket save(Ticket ticket);

    ApiResponse<?> updateAllTickets();

    ApiResponse<?> getUserTickets(String username);

    void processTickets();

    ApiResponse<?> addNewTicketApiResponse(TicketDTO ticketDTO);

    void payoutUsers();

    List<Ticket> getCancelableTickets();
    List<Ticket> getCancelableTickets(String username);

    ApiResponse<?> getCancelableTicketsApiResponse();
    ApiResponse<?> getCancelableTicketsApiResponse(String username);

    Ticket cancelTicket(Integer ticketID);

    ApiResponse<?> cancelTicketApiResponse(Integer ticketID);
}
