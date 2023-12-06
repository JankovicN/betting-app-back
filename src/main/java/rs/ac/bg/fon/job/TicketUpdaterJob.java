package rs.ac.bg.fon.job;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.service.FootballApiService;
import rs.ac.bg.fon.service.TicketService;

@RequiredArgsConstructor
@Component
public class TicketUpdaterJob {

    private static final Logger logger = LoggerFactory.getLogger(TicketUpdaterJob.class);
    private final BetService betService;
    private final TicketService ticketService;
    private final FootballApiService footballApiService;

    @Scheduled(initialDelay = 10 * 1000, fixedRate = 60 * 60 * 1000)
    public void updateBets() {
        try {
            betService.updateAllBets();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Scheduled(initialDelay = 2 * 60 * 1000, fixedRate = 60 * 60 * 1000)
    public void payoutUsers() {
        try {
            ticketService.payoutUsers();
        } catch (Exception e) {
            logger.error("Error while paying out users! \n" + e.getMessage());
        }
    }

    @Scheduled(initialDelay = 30 * 1000, fixedRate = 60 * 60 * 1000)
    public void updateTickets() {
        try {
            ticketService.updateAllTickets();
        } catch (Exception e) {
            logger.error("Error while updating ticket! \n" + e.getMessage());
        }
    }

    @Scheduled(initialDelay = 1000, fixedRate = 1000 * 60)
    public void processTickets() {
        try {
            ticketService.processTickets();
        } catch (Exception e) {
            logger.error("Error while updating ticket! \n" + e.getMessage());
        }
    }
    @Scheduled(initialDelay = 1000 * 60 * 60 * 12, fixedRate = 1000 * 60 * 60 * 24)
    public void fetchNewFixtures() {
        try {
            footballApiService.getFixturesAndOddsFromAPI();
        } catch (Exception e) {
            logger.error("Error while updating ticket! \n" + e.getMessage());
        }
    }

}
