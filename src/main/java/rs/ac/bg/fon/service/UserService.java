package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);


    User getUser(Integer userId);

    List<User> getUsers();

    User registerUser(User user);

    User deleteUser(String username);
    User updateUser(User user);

    ApiResponse<?> deleteUserApiResponse(String username);

    ApiResponse<?> registerUserApiResponse(User user);

    ApiResponse<?> getUsersApiResponse();

    ApiResponse<?> getUserApiResponse(String username);

    ApiResponse<?> addRoleToUserApiResponse(String username, String roleName);

    ApiResponse<?> saveRoleApiResponse(Role role);

    ApiResponse<?> updateUserApiResponse(User user);
}
