package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();

    User registerUser(User user);

    User deleteUser(String username);


}
