package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Transaction;
import rs.ac.bg.fon.repository.TransactionRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Override
    public Double getUserTransactions(String username) {
        return transactionRepository.getUserPayments(username);
    }

    @Override
    public void addTransaction(Transaction Transaction) {
        transactionRepository.saveAndFlush(Transaction);
    }


    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
