package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.service.TicketService;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("ticket")
public class TicketController {

    private final TicketService ticketService;

    @PatchMapping("/update")
    public ResponseEntity<?> updateAllTickets() {
        return ApiResponseUtil.handleApiResponse(ticketService.updateAllTickets());
    }

    @PostMapping("/new")
    public ResponseEntity<?> addNewTicket(@RequestBody TicketDTO ticket) {

        if (ticket == null) {
            return ApiResponseUtil.errorApiResponse("Ticket data is missing!\nContact support for more information!");
        }
        if (ticket.getBets() == null || ticket.getBets().isEmpty()) {
            return ApiResponseUtil.errorApiResponse("Ticket must contain at least one bet!");
        }
        return ApiResponseUtil.handleApiResponse(ticketService.addNewTicketApiResponse(ticket));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> cancelTicket(@RequestParam Integer ticketID) {

        if (ticketID == null) {
            return ApiResponseUtil.errorApiResponse("Ticket data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(ticketService.cancelTicketApiResponse(ticketID));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<?> getTickets(@RequestParam(required = false) Optional<String> date,Pageable pageable) {
        return ApiResponseUtil.handleApiResponse(ticketService.handleGetAllTickets(date, pageable));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUserTickets(@RequestParam String username, @RequestParam(required = false) Optional<String> date, Pageable pageable) {
        if (username == null || username.isBlank()) {
            return ApiResponseUtil.errorApiResponse("User data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(ticketService.handleGetUserTickets(username,date, pageable));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/cancelable")
    public ResponseEntity<?> getCancelableTickets(@RequestParam(required = false) Optional<String> username, Pageable pageable) {
        ApiResponse<?> response = ticketService.handleCancelTicketsAPI(username, pageable);
        return ApiResponseUtil.handleApiResponse(response);
    }

}
