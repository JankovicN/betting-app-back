package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.dtos.User.UserDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserDTOGenerator {
    private static int userCounter = 1;

    public static UserDTO generateUniqueUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(userCounter);
        user.setName("Name" + userCounter);
        user.setSurname("Surname" + userCounter);
        user.setEmail("user" + userCounter + "@example.com");
        user.setBirthday("1999-01-01");
        user.setUsername("username" + userCounter);
        user.setBalance(BigDecimal.valueOf(userCounter * 500));
        return user;
    }

    public static List<UserDTO> generateUniqueUsers(int numOfUsers) {
        List<UserDTO> users = new ArrayList<>();
        for (int i = 0; i < numOfUsers; i++) {
            users.add(generateUniqueUserDTO());
        }
        return users;
    }
}
