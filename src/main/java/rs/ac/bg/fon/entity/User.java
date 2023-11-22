package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;
    @Column(name = "birthday")
    private LocalDate birthday = null;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;


    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @Override
    public String toString() {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(surname)) {
            return super.toString();
        }
        return StringUtils.capitalize(name) + " " + StringUtils.capitalize(surname);
    }

}
