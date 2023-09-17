package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    private final UserService userService;
    private final BetService betService;
    private final TicketService ticketService;

    @PatchMapping("/update")
    public ResponseEntity<?> updateAllTickets(){
        ticketService.updateAllTickets();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/user/tickets/{username}")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable String username){
        return ResponseEntity.ok().body(ticketService.getUserTickets(username));
    }

    @PostMapping(path = "/new/ticket")
    public ResponseEntity<?> addNewTicket(@RequestBody TicketDTO ticket) {

        if (ticket == null) {
            return ResponseEntity.badRequest().body("Ticket is empty!");
        }
        return ApiResponseUtil.handleApiResponse(ticketService.addNewTicketApiResponse(ticket));
    }
}
