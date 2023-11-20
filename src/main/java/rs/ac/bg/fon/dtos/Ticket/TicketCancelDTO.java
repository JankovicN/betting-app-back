package rs.ac.bg.fon.dtos.Ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketCancelDTO implements Serializable {

    private Integer id;
    private Integer userId;
    private String username;
    private String dateOfPlay;
}