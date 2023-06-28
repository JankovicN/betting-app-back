package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dto.BalanceInfo;

public interface BalanceInfoService {

    BalanceInfo getUserBalance(String username);

    Boolean canMakePayment(String username, Double paymentAmount);
}
