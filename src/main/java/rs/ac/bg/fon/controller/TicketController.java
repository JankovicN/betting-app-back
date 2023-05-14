package rs.ac.bg.fon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.service.TicketService;
import rs.ac.bg.fon.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public ResponseEntity<?> addNewTicket(@RequestBody String json) {
        System.out.println(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement responseEl=gson.fromJson(json, JsonElement.class);

        String username = responseEl.getAsJsonObject().get("ticket").getAsJsonObject().get("username").toString();
        User user= userService.getUser(username.substring(1,username.length()-1));
        String wagerStr = responseEl.getAsJsonObject().get("ticket").getAsJsonObject().get("wager").toString();
        BigDecimal wager = new BigDecimal(wagerStr);
        double totalOdds = responseEl.getAsJsonObject().get("ticket").getAsJsonObject().get("totalOdds").getAsDouble();
        String payoutStr = responseEl.getAsJsonObject().get("ticket").getAsJsonObject().get("payout").toString();
        BigDecimal payout = new BigDecimal(payoutStr);
        Ticket ticket = new Ticket();
        ticket.setWager(wager);
        ticket.setOdd(totalOdds);
        ticket.setTotalWin(payout);
        ticket.setState("-");
        ticket.setUser(user);
        ticket.setDate(LocalDateTime.now());
        System.out.println(user);
        System.out.println(ticket);
        JsonArray arr = responseEl.getAsJsonObject().get("ticket").getAsJsonObject().getAsJsonArray("bets");
        ticket=ticketService.save(ticket);
        for (JsonElement jsonElement : arr) {
            int oddID = jsonElement.getAsJsonObject().get("bet").getAsJsonObject().get("id").getAsInt();
            Odd odd = new Odd();
            odd.setId(oddID);
            Bet bet=new Bet();
            bet.setOdd(odd);
            bet.setState("-");
            bet.setTicket(ticket);
            bet= betService.save(bet);
            bet.setTicket(ticket);
            ticket.getBets().add(bet);
        }
        return ResponseEntity.ok().build();
    }
}
