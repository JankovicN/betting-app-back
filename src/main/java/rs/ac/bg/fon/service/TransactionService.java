package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Transaction;

@Service
public interface TransactionService {

    Double getUserTransactions(String username);

    void addTransaction(Transaction transaction);
}
