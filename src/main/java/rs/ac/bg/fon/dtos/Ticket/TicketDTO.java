package rs.ac.bg.fon.dtos.Ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.bg.fon.dtos.Bet.BetDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TicketDTO implements Serializable {

    private BigDecimal wager;
    private Double totalOdd;
    private BigDecimal totalWin;
    private String username;
    private List<BetDTO> bets;

    public TicketDTO() {
        bets = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "TicketDTO object has invalid fields [wager = " + wager
                + ", totalWin = " + totalWin + ", totalOdds = " + totalOdd
                + ", username = " + username + "]";
    }
}
