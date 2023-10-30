package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.repository.BetRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {
    private static final Logger logger = LoggerFactory.getLogger(BetServiceImpl.class);

    private BetRepository betRepository;

    @Override
    public Bet save(Bet bet) {
        try {
            Bet savedBet = betRepository.save(bet);
            logger.info("Successfully saved Bet " + savedBet + "!");
            return savedBet;
        } catch (Exception e) {
            logger.error("Error while trying to save Bet " + bet + "!\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateAllBets() throws Exception {
        try {
            betRepository.updateAllBets();
            logger.info("Successfully updated bets!");
        } catch (Exception e) {
            logger.error("Error while trying to update Bets!\n" + e.getMessage());
            throw new Exception("Error while trying to update Bets!\n" + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void saveBetsForTicket(List<Bet> betList, Ticket ticket) throws Exception {
        try{
            for (Bet bet : betList) {
                if (bet == null || bet.getOdd() == null) {
                    logger.warn("Unable to save Bet [" + bet + "]!");
                    throw new Exception("Unable to save all Bets for Ticket!");
                }
                Odd odd = bet.getOdd();
                bet.setOdd(odd);
                bet.setState(Constants.BET_NOT_FINISHED);
                bet.setTicket(ticket);
                betRepository.save(bet);
            }
        }catch(Exception e){
            if(!e.getMessage().contains("Unable to save all Bets for Ticket")){
                logger.error("Unable to save Bets for ticket, unknown error!\n"+e.getMessage());
            }
            throw new Exception("Unable to save all Bets for Ticket!");
        }
    }
    @Autowired
    public void setBetRepository(BetRepository betRepository) {
        this.betRepository = betRepository;
    }
}
