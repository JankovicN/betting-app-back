package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.User.UserDTO;
import rs.ac.bg.fon.dtos.User.UserRegistrationDTO;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.utility.Utility;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public static User userDtoToUser(UserRegistrationDTO userDTO) throws Exception {
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()
                || userDTO.getBirthday() == null
                || userDTO.getUsername() == null || userDTO.getUsername().isBlank()
                || userDTO.getSurname() == null || userDTO.getSurname().isBlank()
                || userDTO.getName() == null || userDTO.getName().isBlank()
                || userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            throw new Exception("User object has invalid fields [ email = " + userDTO.getEmail() + ", birthday = " + userDTO.getBirthday()
                    + ", username = " + userDTO.getUsername() + ", password = " + userDTO.getPassword()
                    + ", name = " + userDTO.getName() + ", surname = " + userDTO.getSurname() + " ]");
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setBirthday(Utility.parseDate(userDTO.getBirthday()));
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public static UserDTO userToUserDTO(User user) throws Exception {
        if (user.getId() == null || user.getId() < 0
                || user.getBirthday() == null
                || user.getUsername() == null || user.getUsername().isBlank()
                || user.getSurname() == null || user.getSurname().isBlank()
                || user.getName() == null || user.getName().isBlank()
                || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new Exception("User object has invalid fields [id = " + user.getId() + ", email = " + user.getEmail()
                    + ", username = " + user.getUsername() + ", birthday = " + user.getBirthday()
                    + ", name = " + user.getName() + ", surname = " + user.getSurname() + "]");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setBirthday(Utility.formatDate(user.getBirthday()));
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    public static List<UserDTO> userToUserDTO(List<User> users) throws Exception {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(userToUserDTO(user));
        }
        return userDTOS;
    }

    public static User userDtoToUser(UserDTO userDTO) throws Exception {
        if (userDTO.getId() == null || userDTO.getId() < 0
                || userDTO.getBirthday() == null
                || userDTO.getUsername() == null || userDTO.getUsername().isBlank()
                || userDTO.getSurname() == null || userDTO.getSurname().isBlank()
                || userDTO.getName() == null || userDTO.getName().isBlank()
                || userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            throw new Exception("User object has invalid fields [id = " + userDTO.getId() + ", email = " + userDTO.getEmail()
                    + ", username = " + userDTO.getUsername() + ", birthday = " + userDTO.getBirthday()
                    + ", name = " + userDTO.getName() + ", surname = " + userDTO.getSurname() + "]");
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setBirthday(Utility.parseDate(userDTO.getBirthday()));
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
