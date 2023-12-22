package rs.ac.bg.fon.BettingAppBack.util;

import rs.ac.bg.fon.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserGenerator {
    private static int userCounter = 1;

    public static User generateUniqueUser() {
        User user = new User();
        user.setId(userCounter++);
        user.setName("Name" + userCounter);
        user.setSurname("Surname" + userCounter);
        user.setEmail("user" + userCounter + "@example.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setUsername("username" + userCounter);
        user.setPassword("password" + userCounter);
        return user;
    }

    public static List<User> generateUniqueUsers(int numOfUsers){
        List<User> users = new ArrayList<>();
        for (int i = 0;i<numOfUsers;i++){
            users.add(generateUniqueUser());
        }
        return users;
    }
}
