package rs.ac.bg.fon.constants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.entity.BetGroup;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@NoArgsConstructor
public class AllBetGroups {
    private static final Logger logger = LoggerFactory.getLogger(AllBetGroups.class);
    private static final String BET_GROUPS_FILE = "bet_groups.json";
    private List<BetGroup> allBetGroupsList;

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File betGroupFile = new File(Constants.RESOURCE_FOLDER + BET_GROUPS_FILE);
            allBetGroupsList = mapper.readValue(betGroupFile, new TypeReference<List<BetGroup>>() {
            });
            logger.info("Successfully read json file containing all bet groups");
        } catch (IOException e) {
            logger.error("IOException: Error getting all bet groups from JSON file");
            System.out.println("IOException");
        } catch (Exception e) {
            logger.error("Exception: Error getting all bet groups from JSON file");
        }
    }

    public List<BetGroup> getAllBetGroupsList() {
        return allBetGroupsList;
    }

}
