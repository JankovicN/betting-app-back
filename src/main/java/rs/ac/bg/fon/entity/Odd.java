package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "odd")
public class Odd {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "odd_id")
    private Integer id;
    @Column(name="odd_value")
    private BigDecimal odd;
    @Column(name="odd_name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fixture_id")
    private Fixture fixture;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bet_group_id")
    private BetGroup betGroup;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "odd")
    private Bet bet;

}
