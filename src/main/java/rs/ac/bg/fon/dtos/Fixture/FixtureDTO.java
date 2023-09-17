package rs.ac.bg.fon.dtos.Fixture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.bg.fon.dtos.BetGroup.BetGroupDTO;
import rs.ac.bg.fon.dtos.Team.TeamDTO;
import rs.ac.bg.fon.entity.Fixture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FixtureDTO implements Serializable {

    private Integer id;

    private Date date;

    private TeamDTO home;

    private TeamDTO away;

    private int homeGoals;

    private int awayGoals;

    private String state = "NS";

    List<BetGroupDTO> betGroupList;

    public FixtureDTO() {
        betGroupList =new ArrayList<>();
    }
}
