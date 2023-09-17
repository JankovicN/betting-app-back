package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
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
                throw new Exception("Invalid bet!");
            }
            Odd odd = bet.getOdd();
            bet.setOdd(odd);
            bet.setState(Constants.BET_NOT_FINISHED);
            bet.setTicket(ticket);
        }
    }
}
