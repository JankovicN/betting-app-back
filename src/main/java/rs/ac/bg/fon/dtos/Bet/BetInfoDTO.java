package rs.ac.bg.fon.dtos.Bet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BetInfoDTO {

    private Integer id;
    private String state;
    private BigDecimal odd;
    private String name;
    private String betGroupName;
    private String fixtureDate;
    private String home;
    private String away;
    private String result;


    @Override
    public String toString() {
        return "BetInfoDTO object has invalid fields [id = " + id
                + ", state = " + state + ", odd = " + odd
                + ", name = " + name + ", betGroupName = " + betGroupName
                + ", fixtureDate = " + fixtureDate + ", home = " + home
                + ", away = " + away + ", result = " + result + "]";
    }
}
