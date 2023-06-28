package rs.ac.bg.fon.service;

import org.springframework.beans.factory.annotation.Autowired;
import rs.ac.bg.fon.dto.BalanceInfo;
import rs.ac.bg.fon.entity.User;

public class BalanceInfoServiceImpl implements BalanceInfoService {

    TransactionService transactionService;
    TicketService ticketService;
    UserService userService;

    @Override
    public BalanceInfo getUserBalance(String username) {
        Double balance = transactionService.getUserTransactions(username) + ticketService.getTotalWinAmountForUser(username) - ticketService.getWagerAmoutForUser(username);
        User user = userService.getUser(username);
        BalanceInfo balanceInfo = new BalanceInfo(user, balance);
        return balanceInfo;
    }

    @Override
    public Boolean canMakePayment(String username, Double paymentAmount) {
        BalanceInfo balanceInfo = getUserBalance(username);
        return balanceInfo.getBalance() >= paymentAmount;
    }

    @Autowired
    public void setPaymentService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
