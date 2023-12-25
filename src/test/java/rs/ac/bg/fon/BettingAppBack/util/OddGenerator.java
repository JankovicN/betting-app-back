package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.OddGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OddGenerator {
    private static int oddCounter = 1;

    public static Odd generateUniqueOdd() {
        Odd odd = new Odd();
        odd.setId(oddCounter++);
        odd.setName(generateUniqueOddName());
        odd.setOddValue(BigDecimal.valueOf(oddCounter+1.23));
        odd.setBets(generateBetsForOdd(odd));
        return odd;
    }
    public static List<Odd> generateUniqueOdds(int numOfOdds){
        List<Odd> odds = new ArrayList<>();
        for (int i = 0;i<numOfOdds;i++){
            odds.add(generateUniqueOdd());
        }
        return odds;
    }
    private static String generateUniqueOddName() {
        return "Odd" + oddCounter;
    }

    private static List<Bet> generateBetsForOdd(Odd odd) {
        List<Bet> bets = BetGenerator.generateUniqueBets(2);
        for (Bet bet : bets) {
            bet.setOdd(odd);
        }
        return bets;
    }
    public static List<Odd> setFixtureForOdds(List<Odd> odds, Fixture fixture){
        for (Odd odd : odds) {
            odd.setFixture(fixture);
        }
        return odds;
    }
    public static List<Odd> setFixtureForOdds(List<Odd> odds){
        for (Odd odd : odds) {
            odd.setFixture(FixtureGenerator.generateUniqueFixture());
        }
        return odds;
    }
    public static List<Odd> setOddGroupForOdds(List<Odd> odds, OddGroup oddGroup){
        for (Odd odd : odds) {
            odd.setOddGroup(oddGroup);
        }
        return odds;
    }
}
