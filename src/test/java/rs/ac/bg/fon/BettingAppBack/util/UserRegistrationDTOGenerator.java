package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.dtos.User.UserRegistrationDTO;

import java.util.ArrayList;
import java.util.List;

public class UserRegistrationDTOGenerator {
    private static int userCounter = 1;

    public static UserRegistrationDTO generateUniqueUserDTO() {
        UserRegistrationDTO user = new UserRegistrationDTO();
        user.setName("Name" + userCounter);
        user.setSurname("Surname" + userCounter);
        user.setEmail("user" + userCounter + "@example.com");
        user.setBirthday("1999-01-01");
        user.setUsername("username" + userCounter);
        user.setPassword("password" + userCounter);
        return user;
    }

    public static List<UserRegistrationDTO> generateUniqueUsers(int numOfUsers){
        List<UserRegistrationDTO> users = new ArrayList<>();
        for (int i = 0;i<numOfUsers;i++){
            users.add(generateUniqueUserDTO());
        }
        return users;
    }
}
