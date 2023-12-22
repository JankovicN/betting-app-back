package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.entity.Role;

public class RoleGenerator {

    public static Role getClientRole() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_CLINET");
        return role;
    }
    public static Role getAdminRole() {
        Role role = new Role();
        role.setId(2);
        role.setName("ROLE_ADMIN");
        return role;
    }
}
