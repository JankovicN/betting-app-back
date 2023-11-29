package rs.ac.bg.fon.constants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.entity.OddGroup;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@NoArgsConstructor
public class AllOddGroups {
    private static final Logger logger = LoggerFactory.getLogger(AllOddGroups.class);
    private static final String ODD_GROUPS_FILE = "odd_groups.json";
    private List<OddGroup> allOddGroupsList;

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File oddGroupFile = new File(Constants.RESOURCE_FOLDER + ODD_GROUPS_FILE);
            allOddGroupsList = mapper.readValue(oddGroupFile, new TypeReference<List<OddGroup>>() {
            });
            logger.info("Successfully read json file containing all Odd Groups");
        } catch (IOException e) {
            logger.error("IOException: Error getting all Odd Groups from JSON file");
            System.out.println("IOException");
        } catch (Exception e) {
            logger.error("Exception: Error getting all Odd Groups from JSON file");
        }
    }

    public List<OddGroup> getAllOddGroupsList() {
        return allOddGroupsList;
    }

}
