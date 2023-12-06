package rs.ac.bg.fon.service;

import org.springframework.data.domain.Pageable;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.utility.ApiResponse;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TicketService {

    ApiResponse<?> updateAllTickets();

    ApiResponse<?> getUserTickets(String username, Pageable pageable);

    ApiResponse<?> getUserTickets(String username, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    ApiResponse<?> getAllTickets(Pageable pageable);


    ApiResponse<?> getAllTickets(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    void processTickets();

    ApiResponse<?> addNewTicketApiResponse(TicketDTO ticketDTO);

    void payoutUsers();

    ApiResponse<?> handleCancelTicketsAPI(Optional<String> username, Pageable pageable);

    Ticket cancelTicket(Integer ticketID);

    ApiResponse<?> cancelTicketApiResponse(Integer ticketID);

    ApiResponse<?> handleGetUserTickets(String username, Optional<String> date, Pageable pageable);

    ApiResponse<?> handleGetAllTickets(Optional<String> date, Pageable pageable);
}
