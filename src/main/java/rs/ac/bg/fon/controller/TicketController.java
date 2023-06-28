package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.entity.Transaction;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.service.*;
import rs.ac.bg.fon.utility.ApiResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/ticket")
public class TicketController {

    private final UserService userService;
    private final TicketService ticketService;
    private final BetService betService;
    private final BalanceInfoService balanceInfoService;
    private final TransactionService transactionService;

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateAllTickets() {

        try {
            ticketService.updateAllTickets();
            return ResponseEntity.ok().body(new ApiResponse<>(null, "Successfully updated tickets!", ""));
        } catch (Exception e) {
            System.out.println("Error in: updateAllTickets() \n" + e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null, "", "Error while updating tickets.\nPlease try again later."));
        }
    }

    @GetMapping("/get/user/tickets/{username}")
    public ResponseEntity<ApiResponse<List<Ticket>>> getUserTickets(@PathVariable String username) {
        try {
            List<Ticket> userTickets = ticketService.getUserTickets(username);
            return ResponseEntity.ok().body(new ApiResponse<>(userTickets, "", ""));
        } catch (Exception e) {
            System.out.println("Error in: getUserTickets() \n" + e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null, "", "Error while getting user tickets.\nPlease try again later."));
        }
    }

    @PatchMapping("/cancel/{ticketId}")
    public ResponseEntity<ApiResponse<?>> cancelTicket(@PathVariable int ticketId) {
        try {
            ticketService.cancelTicket(ticketId);
            Optional<Ticket> userTicket = ticketService.getTicketById(ticketId);

            if (!userTicket.isPresent()) {
                throw new Exception("Error while canceling ticket!");
            }
            if (!userTicket.get().getState().equals("CANCELED")) {
                throw new Exception("Time for canceling ticket has passed!");
            }

            Transaction transaction = new Transaction();
            transaction.setAmount(userTicket.get().getWager().doubleValue());
            transaction.setUser(userTicket.get().getUser());
            transactionService.addTransaction(transaction);

            return ResponseEntity.ok().body(new ApiResponse<>(null, "Successfully canceled ticket!", ""));
        } catch (Exception e) {
            System.out.println("Error in: cancelTicket() \n" + e.getMessage());
            String errorMessage = e.getMessage().contains("canceling ticket") ? e.getMessage() : "Error while playing ticket.\nPlease try again later.";
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null, "", errorMessage));
        }
    }

    @PostMapping(path = "/new/ticket")
    public ResponseEntity<ApiResponse<?>> addNewTicket(@RequestBody String ticketJson) {
        System.out.println(ticketJson);
        try {

            Ticket ticket = ticketService.createTicketFromJsonString(ticketJson);
            User user = userService.createUserFromTicketJson(ticketJson);

            if (ticket.getWager().compareTo(BigDecimal.ZERO) <= 0) {
                throw new Exception("Wager must be greater than 0!");
            }

            if (balanceInfoService.canMakePayment(user.getUsername(), ticket.getWager().doubleValue())) {
                throw new Exception("Insufficient funds!");
            }

            List<Bet> betList = betService.getBetsFromTicket(ticketJson, ticket);
            ticket.setBets(betList);

            ticketService.save(ticket);
            return ResponseEntity.ok().body(new ApiResponse<>(null, "Ticket played successfully!", ""));
        } catch (Exception e) {
            System.out.println("Error in: addNewTicket() \n" + e.getMessage());
            String errorMessage = (e.getMessage().equals("Some game has started, create a new ticket!") ||
                    e.getMessage().equals("Wager must be greater than 0!") ||
                    e.getMessage().equals("Insufficient funds!") ||
                    e.getMessage().equals("Error getting odds!"))
                    ? e.getMessage()
                    : "Error while playing ticket.\nPlease try again later.";
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null, "", errorMessage));
        }
    }
}
