package rs.ac.bg.fon.dtos.OddGroup;

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
public class OddGroupDTO implements Serializable {

    private Integer id;

    private String name;

    private List<OddDTO> odds;

    public OddGroupDTO() {
        odds = new ArrayList<>();
    }
}
