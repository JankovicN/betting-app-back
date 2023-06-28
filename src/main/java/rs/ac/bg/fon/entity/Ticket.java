package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name="ticket_id")
    private Integer id;
    @Column(name="wager")
    private BigDecimal wager;
    @Column(name="odd")
    private double odd;
    @Column(name="total_win")
    private BigDecimal totalWin;
    @Column(name="date")
    private LocalDateTime date;
    @Column(name="state")
    private String state="UNPROCESSED";

    @JsonIgnore
    @OneToMany(mappedBy="ticket")
    private List<Bet> bets;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public List<Bet> getBets() {
        if(this.bets==null){
            bets=new ArrayList<>();
        }
        return bets;
    }
}
