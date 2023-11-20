package rs.ac.bg.fon.service;

import org.springframework.data.domain.Pageable;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.Optional;

public interface TicketService {

    ApiResponse<?> updateAllTickets();

    ApiResponse<?> getUserTickets(String username, Pageable pageable);

    ApiResponse<?> getAllTickets(Pageable pageable);

    void processTickets();

    ApiResponse<?> addNewTicketApiResponse(TicketDTO ticketDTO);

    void payoutUsers();

    ApiResponse<?> handleCancelTicketsAPI(Optional<String> username, Pageable pageable);

    Ticket cancelTicket(Integer ticketID);

    ApiResponse<?> cancelTicketApiResponse(Integer ticketID);
}
