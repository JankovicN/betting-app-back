package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.repository.BetRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BetServiceImpl implements BetService{
    private static final Logger logger = LoggerFactory.getLogger(BetServiceImpl.class);

    private BetRepository betRepository;

    @Autowired
    public void setBetRepository(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    @Override
    public Bet save(Bet bet) {
        return betRepository.save(bet);
    }

    @Override
    public void updateAllBets() {
        betRepository.updateAllBets();
    }

    @Override
    public void saveBetsForTicket(List<Bet> betList, Ticket ticket) throws Exception {
        for (Bet bet : betList) {
            if (bet ==null || bet.getOdd()==null){
                throw new Exception("Unable to save all Bets for Ticket!");
            }
            Odd odd = bet.getOdd();
            bet.setOdd(odd);
            bet.setState(Constants.BET_NOT_FINISHED);
            bet.setTicket(ticket);
        }
    }
}
