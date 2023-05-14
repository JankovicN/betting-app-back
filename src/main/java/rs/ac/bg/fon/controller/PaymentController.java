package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.service.PaymentService;
import rs.ac.bg.fon.service.TicketService;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final TicketService ticketService;


    @GetMapping("/balance/{userId}")
    public ResponseEntity<Double> getBalanceForUser(@PathVariable int userId){
        Double balance = paymentService.getUserPayments(userId)+ ticketService.getTotalWinAmountForUser(userId) - ticketService.getWagerAmoutForUser(userId);
        return ResponseEntity.ok().body(balance);
    }
}
