package rs.ac.bg.fon.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.bg.fon.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceInfo {

    private User user;
    private Double balance;
}
