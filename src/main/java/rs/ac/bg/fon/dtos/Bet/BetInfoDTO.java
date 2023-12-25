package rs.ac.bg.fon.dtos.Bet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BetInfoDTO {

    private Integer id;
    private String state;
    private BigDecimal odd;
    private String name;
    private String oddGroupName;
    private String fixtureDate;
    private String home;
    private String away;
    private String result;


    @Override
    public String toString() {
        return "BetInfoDTO object has invalid fields [id = " + id
                + ", state = " + state + ", odd = " + odd
                + ", name = " + name + ", oddGroupName = " + oddGroupName
                + ", fixtureDate = " + fixtureDate + ", home = " + home
                + ", away = " + away + ", result = " + result + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BetInfoDTO other = (BetInfoDTO) obj;
        // Compare individual fields for equality
        return Objects.equals(id, other.id) &&
                Objects.equals(state, other.state) &&
                Objects.equals(odd, other.odd) &&
                Objects.equals(name, other.name) &&
                Objects.equals(oddGroupName, other.oddGroupName) &&
                Objects.equals(fixtureDate, other.fixtureDate) &&
                Objects.equals(home, other.home) &&
                Objects.equals(away, other.away) &&
                Objects.equals(result, other.result);
    }
}
