package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.service.TicketService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("ticket")
public class TicketController {

    private final TicketService ticketService;

    @PatchMapping("/update")
    public ResponseEntity<?> updateAllTickets() {
        return ApiResponseUtil.handleApiResponse(ticketService.updateAllTickets());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUserTickets(@RequestParam String username) {
        if (username == null || username.isBlank()) {
            return ApiResponseUtil.errorApiResponse("User data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(ticketService.getUserTickets(username));
    }

    @PostMapping(path = "/new")
    public ResponseEntity<?> addNewTicket(@RequestBody TicketDTO ticket) {

        if (ticket == null) {
            return ApiResponseUtil.errorApiResponse("Ticket data is missing!\nContact support for more information!");
        }
        if (ticket.getBets() == null || ticket.getBets().isEmpty() ){
            return ApiResponseUtil.errorApiResponse("Ticket must contain at least one bet!");
        }
        return ApiResponseUtil.handleApiResponse(ticketService.addNewTicketApiResponse(ticket));
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> addNewTicket(@RequestParam Integer ticketID) {

        if (ticketID == null) {
            return ApiResponseUtil.errorApiResponse("Ticket data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(ticketService.cancelTicketApiResponse(ticketID));
    }

    @GetMapping(path = "/get/cancelable")
    public ResponseEntity<?> getCancelableTickets(@RequestParam(required = false) Optional<String> username) {
        if (username.isPresent()) {
            // Logic when ticketID is present
            String usernameValue = username.get();
            return ApiResponseUtil.handleApiResponse(ticketService.getCancelableTicketsApiResponse(usernameValue));
            // Additional functionality based on the presence of ticketID
        } else {
            return ApiResponseUtil.handleApiResponse(ticketService.getCancelableTicketsApiResponse());
            // Logic when ticketID is not present
        }
    }

}
