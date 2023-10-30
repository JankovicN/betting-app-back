package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.User.UserDTO;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.utility.Utility;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {


    public UserDTO userToUserDTO(User user) throws Exception {
        if (user.getId() == null || user.getId() < 0
                || user.getBirthday() == null
                || user.getUsername() == null || user.getUsername().isBlank()
                || user.getSurname() == null || user.getSurname().isBlank()
                || user.getName() == null || user.getName().isBlank()
                || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new Exception("User object has invalid fields [id = " + user.getId()+ ", email = " + user.getEmail()
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

    public List<UserDTO> userToUserDTO(List<User> users) throws Exception{
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user: users) {
            userDTOS.add(userToUserDTO(user));
        }
        return userDTOS;
    }
}
