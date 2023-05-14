package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="bet")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bet_id")
    private Integer id;
    @Column(name="state")
    private String state="NS";

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "odds_id", nullable = false)
    private Odd odd;

    @ManyToOne
    @JoinColumn(name="ticket_id", nullable=false)
    private Ticket ticket;

}
