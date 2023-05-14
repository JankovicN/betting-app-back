package rs.ac.bg.fon.job;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.service.TicketService;

@RequiredArgsConstructor
public class TicketUpdaterJob {

    private final BetService betService;
    private final TicketService ticketService;
    private static final Logger logger = LoggerFactory.getLogger(TicketUpdaterJob.class);

    @Scheduled(initialDelay = 10 * 1000, fixedRate = 60 * 60 * 1000)
    public void updateBets(){
        try {
            betService.upadateAllBets();
        }catch (Exception e){
            logger.error("Error while updating bets on ticket! \n"+ e.getMessage());
        }
    }


    @Scheduled(initialDelay = 30 * 1000, fixedRate = 60 * 60 * 1000)
    public void updateTickets(){
        try {
            ticketService.updateAllTickets();
        }catch (Exception e){
            logger.error("Error while updating ticket! \n"+ e.getMessage());
        }
    }

    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    public void proccessTickets(){
        try {
            ticketService.proccessTickets();
        }catch (Exception e){
            logger.error("Error while updating ticket! \n"+ e.getMessage());
        }
    }

}
