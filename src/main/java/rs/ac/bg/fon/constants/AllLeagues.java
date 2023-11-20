package rs.ac.bg.fon.constants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.entity.League;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@NoArgsConstructor
public class AllLeagues {
    private static final Logger logger = LoggerFactory.getLogger(AllLeagues.class);
    private static final String LEAGUE_FILE = "leagues.json";
    private List<League> allLeagues;

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File leagueFile = new File(Constants.RESOURCE_FOLDER + LEAGUE_FILE);
            allLeagues = mapper.readValue(leagueFile, new TypeReference<List<League>>() {
            });
            logger.info("Successfully read json file containing all leagues");
        } catch (IOException e) {
            logger.error("IOException: Error getting all leagues from JSON file");
            System.out.println("IOException");
        } catch (Exception e) {
            logger.error("Exception: Error getting all leagues from JSON file");
        }
    }

    public List<League> getAllLeagues() {
        return allLeagues;
    }
}
