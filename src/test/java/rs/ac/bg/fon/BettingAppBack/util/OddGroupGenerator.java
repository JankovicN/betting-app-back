package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.OddGroup;

import java.util.ArrayList;
import java.util.List;

public class OddGroupGenerator {
    private static int oddGroupCounter = 1;

    public static OddGroup generateUniqueOddGroup() {
        OddGroup oddGroup = new OddGroup();
        oddGroup.setId(oddGroupCounter++);
        oddGroup.setName(generateUniqueOddGroupName());
        oddGroup.setOdds(generateOddsForOddGroup(oddGroup));
        return oddGroup;
    }
    private static List<Odd> generateOddsForOddGroup(OddGroup oddGroup) {
        List<Odd> odds = OddGenerator.generateUniqueOdds(2);
        for (Odd odd : odds) {
            odd.setOddGroup(oddGroup);
        }
        return odds;
    }
    private static String generateUniqueOddGroupName() {
        return "OddGroup" + oddGroupCounter;
    }
    public static List<OddGroup> generateUniqueOddGroups(int numOfOddGroups){
        List<OddGroup> oddGroups = new ArrayList<>();
        for (int i = 0;i<numOfOddGroups;i++){
            oddGroups.add(generateUniqueOddGroup());
        }
        return oddGroups;
    }
}
