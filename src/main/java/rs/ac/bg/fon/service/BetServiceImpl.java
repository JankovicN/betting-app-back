package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.repository.BetRepository;

import javax.transaction.Transactional;

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
    public void upadateAllBets() {
        betRepository.updateAllBets();
    }
}
