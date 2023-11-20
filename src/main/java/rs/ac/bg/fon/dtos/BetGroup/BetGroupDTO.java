package rs.ac.bg.fon.dtos.BetGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.bg.fon.dtos.Odd.OddDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BetGroupDTO implements Serializable {

    private Integer id;

    private String name;

    private List<OddDTO> odds;

    public BetGroupDTO() {
        odds = new ArrayList<>();
    }
}
