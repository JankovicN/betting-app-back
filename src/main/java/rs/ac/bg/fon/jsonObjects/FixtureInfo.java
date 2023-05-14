package rs.ac.bg.fon.jsonObjects;

import lombok.Getter;
import lombok.Setter;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.entity.Odd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FixtureInfo implements Serializable {
    Fixture fixture;
    List<BetGroup> bets;

    public FixtureInfo() {
        bets=new ArrayList<>();
    }
    public void clearFields(){
        fixture.setLeague(new League());
        fixture.setOdds(new ArrayList<>());
    }

    public void addBets(List<Odd> odds) {
        BetGroup group = new BetGroup();
        for (int i = 0; i < odds.size(); i++) {
            if(i==0){
                group = new BetGroup();
                group.setId(1);
                group.setName("Match Winner");
            }
            if(i==3){
                bets.add(group);
                group = new BetGroup();
                group.setId(5);
                group.setName("Goals Over/Under");

            }
            if(i==15){
                bets.add(group);
                group = new BetGroup();
                group.setId(8);
                group.setName("Both Teams Score");

            }
            if(i==17){
                bets.add(group);
                group = new BetGroup();
                group.setId(12);
                group.setName("Double Chance");

            }
            if(i==20){
                bets.add(group);
                group = new BetGroup();
                group.setId(38);
                group.setName("Odd/Even");

            }
            group.getOdds().add(odds.get(i));
            if(i==odds.size()-1){
                bets.add(group);
            }
        }

    }
}
