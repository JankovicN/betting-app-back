package rs.ac.bg.fon.scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.service.FootballApiService;
import rs.ac.bg.fon.service.TicketService;

@RequiredArgsConstructor
@Component
public class TicketUpdaterScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TicketUpdaterScheduler.class);
    private final BetService betService;
    private final TicketService ticketService;
    private final FootballApiService footballApiService;
    @Async
    @Scheduled(initialDelay = 10 * 1000, fixedRate = 60 * 60 * 1000)
    public void updateBetsJob() {
        try {
            betService.updateAllBets();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    @Async
    @Scheduled(initialDelay = 2 * 60 * 1000, fixedRate = 60 * 60 * 1000)
    public void payoutUsersJob() {
        try {
            ticketService.payoutUsers();
        } catch (Exception e) {
            logger.error("Error while paying out users! \n" + e.getMessage());
        }
    }
    @Async
    @Scheduled(initialDelay = 30 * 1000, fixedRate = 60 * 60 * 1000)
    public void updateTicketsJob() {
        try {
            ticketService.updateAllTickets();
        } catch (Exception e) {
            logger.error("Error while updating ticket! \n" + e.getMessage());
        }
    }
    @Async
    @Scheduled(initialDelay = 1000, fixedRate = 1000 * 60)
    public void processTicketsJob() {
        try {
            ticketService.processTickets();
        } catch (Exception e) {
            logger.error("Error while updating ticket! \n" + e.getMessage());
        }
    }
    @Async
    @Scheduled(initialDelay = 1000 * 60 * 60 * 12, fixedRate = 1000 * 60 * 60 * 24)
    public void fetchNewFixturesJob() {
        try {
            footballApiService.getFixturesAndOddsFromAPI();
        } catch (Exception e) {
            logger.error("Error while updating ticket! \n" + e.getMessage());
        }
    }

}
