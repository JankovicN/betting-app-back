package rs.ac.bg.fon.job;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.service.FootballApiService;
import rs.ac.bg.fon.service.TicketService;

@Configuration
@RequiredArgsConstructor
public class FootballApiJob {
    private final FootballApiService footballApiService;
    private static final Logger logger = LoggerFactory.getLogger(TicketUpdaterJob.class);

    @Scheduled(initialDelay = 24 * 60 * 60 * 1000, fixedRate = 24 * 60 * 60 * 1000)
    public void getFixturesFromApiJob(){
        try {
            footballApiService.getFixturesFromAPI();
        }catch (Exception e){
            logger.error("Error while getting fixtures from API! \n"+ e.getMessage());
        }
    }
    @Scheduled(initialDelay = 24 * 60 * 60 * 1000, fixedRate = 24 * 60 * 60 * 1000)
    public void getOddsFromApiJob(){
        try {
            footballApiService.getOddsFromAPI();
        }catch (Exception e){
            logger.error("Error while getting odds from API! \n"+ e.getMessage());
        }
    }
}
