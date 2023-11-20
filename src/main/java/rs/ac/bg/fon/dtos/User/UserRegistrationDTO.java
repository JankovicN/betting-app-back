package rs.ac.bg.fon.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO implements Serializable {

    private String name;
    private String surname;
    private String birthday;
    private String email;
    private String username;
    private String password;
}
