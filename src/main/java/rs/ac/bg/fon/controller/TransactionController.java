package rs.ac.bg.fon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.dto.BalanceInfo;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.entity.Transaction;
import rs.ac.bg.fon.service.BalanceInfoService;
import rs.ac.bg.fon.service.TicketService;
import rs.ac.bg.fon.service.TransactionService;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/payment")
public class TransactionController {

    private final TransactionService transactionService;
    private final BalanceInfoService balanceInfoService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Transaction>> saveTransactionForUser(@RequestBody String transactionJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Transaction transaction = mapper.readValue(transactionJson, Transaction.class);
            if(transaction.getAmount()<0 && !balanceInfoService.canMakePayment(transaction.getUser().getUsername(), -transaction.getAmount())){
                throw new Exception("You don't have enough funds to make this transaction!");
            }
            transactionService.addTransaction(transaction);
            return ResponseEntity.ok().body(new ApiResponse<>(transaction,"Successfully transaction.\n User balance is updated.",""));
        } catch (Exception e) {
            String errorMessage = e.getMessage().contains("You don't have enough funds to make this transaction") ? e.getMessage() : "Error while executing transaction.\nPlease try again later.";
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null,"",errorMessage));
        }
    }

    @GetMapping("/balance/{username}")
    public ResponseEntity<ApiResponse<BalanceInfo>> getBalanceForUser(@PathVariable String username) {
        try {
            BalanceInfo balanceInfo=balanceInfoService.getUserBalance(username);
            return ResponseEntity.ok().body(new ApiResponse<>(balanceInfo,"",""));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null,"","Error while getting user balance.\nPlease try again later."));
        }
    }
}
