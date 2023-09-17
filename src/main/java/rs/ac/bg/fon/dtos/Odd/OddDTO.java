package rs.ac.bg.fon.dtos.Odd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OddDTO implements Serializable {

    private Integer id;
    private BigDecimal odd;
    private String name;

}
