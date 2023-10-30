package rs.ac.bg.fon.dtos.Ticket;

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
public class TicketBasicDTO implements Serializable {

    private Integer id;
    private String dateOfPlay;
    private BigDecimal wager;
    private Double totalOdd;
    private BigDecimal totalWin;
    private String state;
}
