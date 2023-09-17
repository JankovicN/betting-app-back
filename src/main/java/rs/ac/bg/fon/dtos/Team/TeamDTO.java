package rs.ac.bg.fon.dtos.Team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO implements Serializable {

    private Integer id;
    private String name;

}
