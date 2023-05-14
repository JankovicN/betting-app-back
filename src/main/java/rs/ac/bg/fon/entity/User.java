package rs.ac.bg.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name="user_id")
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="surname")
    private String surname;
    @Column(name="email")
    private String email;
    @Column(name="birthday")
    private Date birthday=null;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;


    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();


}
