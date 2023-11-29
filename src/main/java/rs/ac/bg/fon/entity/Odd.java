package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

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
    @Column(name = "odd_value")
    private BigDecimal odd;
    @Column(name = "odd_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixture_id")
    private Fixture fixture;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odd_group_id")
    private OddGroup oddGroup;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "odd")
    private List<Bet> bet;

    @Override
    public String toString() {
        if (StringUtils.isBlank(name)) {
            return super.toString();
        }
        return name + " " + odd;
    }
}
