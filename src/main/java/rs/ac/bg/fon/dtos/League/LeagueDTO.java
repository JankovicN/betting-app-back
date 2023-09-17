package rs.ac.bg.fon.dtos.League;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LeagueDTO implements Serializable {

    private Integer id;

    private String name;

    private List<FixtureDTO> fixtures;

    public LeagueDTO() {
        fixtures = new ArrayList<>();
    }
}
