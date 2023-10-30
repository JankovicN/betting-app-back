package rs.ac.bg.fon.dtos.Fixture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.BetGroup.BetGroupDTO;
import rs.ac.bg.fon.dtos.Team.TeamDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FixtureDTO implements Serializable {

    private Integer id;

    private String date;

    private TeamDTO home;

    private TeamDTO away;

    private int homeGoals;

    private int awayGoals;

    private String state = Constants.FIXTURE_NOT_STARTED;

    List<BetGroupDTO> betGroupList;

    public FixtureDTO() {
        betGroupList =new ArrayList<>();
    }
}
