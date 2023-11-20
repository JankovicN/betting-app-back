package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.bg.fon.constants.Constants;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bet")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bet_id")
    private Integer id;
    @Column(name = "state")
    private String state = Constants.BET_NOT_FINISHED;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "odds_id", nullable = false)
    private Odd odd;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

}
