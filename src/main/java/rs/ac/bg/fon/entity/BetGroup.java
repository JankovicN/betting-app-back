package rs.ac.bg.fon.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bet_group")
public class BetGroup {
    @Id
    @Column(name = "bet_group_id")
    private Integer id;
    @Column(name = "bet_group_name")
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "betGroup", fetch = FetchType.LAZY)
    private List<Odd> odds;

    public List<Odd> getOdds() {
        if (odds == null) {
            odds = new ArrayList<>();
        }
        return odds;
    }

    @Override
    public String toString() {
        if (StringUtils.isBlank(name)) {
            return super.toString();
        }
        return name;
    }
}
