package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id")
    private Integer id;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "payment_type")
    private String paymentType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
