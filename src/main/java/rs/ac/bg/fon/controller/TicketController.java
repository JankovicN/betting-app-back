package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.service.TicketService;
import rs.ac.bg.fon.service.UserService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/ticket")
public class TicketController {

    private final TicketService ticketService;

    @PatchMapping("/update")
    public ResponseEntity<?> updateAllTickets() {
        return ApiResponseUtil.handleApiResponse(ticketService.updateAllTickets());
    }

    @GetMapping("/get/user/tickets/{username}")
    public ResponseEntity<?> getUserTickets(@PathVariable String username) {
        if (username == null || username.isBlank()) {
            return ApiResponseUtil.errorApiResponse("User data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(ticketService.getUserTickets(username));
    }

    @PostMapping(path = "/new/ticket")
    public ResponseEntity<?> addNewTicket(@RequestBody TicketDTO ticket) {

        if (ticket == null) {
            return ApiResponseUtil.errorApiResponse("Ticket data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(ticketService.addNewTicketApiResponse(ticket));
    }
}
