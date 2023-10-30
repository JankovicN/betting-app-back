package rs.ac.bg.fon.dtos.User;

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
public class UserDTO   implements Serializable {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String birthday;
    private String username;
    private BigDecimal balance;
}
