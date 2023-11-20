package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Bet.BetInfoDTO;
import rs.ac.bg.fon.entity.*;
import rs.ac.bg.fon.mappers.BetMapper;
import rs.ac.bg.fon.repository.BetRepository;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {
    private static final Logger logger = LoggerFactory.getLogger(BetServiceImpl.class);
    private final BetRepository betRepository;

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
        try {
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
        } catch (Exception e) {
            if (!e.getMessage().contains("Unable to save all Bets for Ticket")) {
                logger.error("Unable to save Bets for ticket, unknown error!\n" + e.getMessage());
            }
            throw new Exception("Unable to save all Bets for Ticket!");
        }
    }

    @Transactional
    private List<BetInfoDTO> getBetsForTicket(Integer ticketId) {
        try {
            List<Bet> betList = betRepository.findAllByTicketId(ticketId);
            logger.info("Successfully got bets for ticket id = " + ticketId + "!\n" + betList);
            List<BetInfoDTO> betInfoDTOS = new ArrayList<>();
            for (Bet bet : betList) {
                if (bet.getOdd() == null || bet.getOdd().getId() == null) {
                    return new ArrayList<>();
                }
                Odd odd = bet.getOdd();
                Fixture fixture = odd.getFixture();
                BetGroup betGroup = odd.getBetGroup();
                BetInfoDTO betInfoDTO = new BetInfoDTO();
                try {
                    betInfoDTO = BetMapper.betToBetInfoDTO(bet, betGroup, odd, fixture);
                    betInfoDTOS.add(betInfoDTO);
                } catch (Exception e) {
                    logger.error("Error creating betInfoDTO " + betInfoDTO + "!\n" + e);
                    return new ArrayList<>();
                }
            }
            return betInfoDTOS;
        } catch (Exception e) {
            logger.error("Error getting bets for ticket id = " + ticketId + "!\n" + e);
            return new ArrayList<>();
        }
    }

    @Override
    public ApiResponse<?> getBetsForTicketApiResponse(Integer ticketId) {
        ApiResponse<List<BetInfoDTO>> response = new ApiResponse<>();
        try {
            List<BetInfoDTO> betList = getBetsForTicket(ticketId);
            response.setData(betList);
            logger.info("Successfully got bets for ticket id = " + ticketId + "!\n" + betList);
        } catch (Exception e) {
            response.addErrorMessage("Error getting bets for ticket id = " + ticketId + "!");
            logger.error("Error getting bets for ticket id = " + ticketId + "!\n" + e);
        }
        return response;
    }
}
