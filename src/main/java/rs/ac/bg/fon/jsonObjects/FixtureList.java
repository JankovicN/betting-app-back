package rs.ac.bg.fon.jsonObjects;

import lombok.Getter;
import lombok.Setter;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FixtureList implements Serializable {

    League league;
    List<FixtureInfo> fixtures;

    public FixtureList() {
        fixtures=new ArrayList<>();
    }

    public boolean isLeague(int id){
        return id==league.getId();
    }

    public void setLeague(League league) {
        this.league = league;
    }


    public void addFixtures(List<Fixture> fixes) {
        for (Fixture f : fixes) {
            if(f.getLeague().getId()==this.league.getId()){
                FixtureInfo fixtureInfo= new FixtureInfo();
                fixtureInfo.setFixture(f);
                fixtureInfo.addBets(f.getOdds());
                this.fixtures.add(fixtureInfo);
            }
        }
    }
    public void clearFields(){
        for (FixtureInfo fixture: fixtures) {
            fixture.clearFields();
        }
    }
}
