package rs.ac.bg.fon.service;

import org.springframework.data.domain.Pageable;
import rs.ac.bg.fon.dtos.User.UserDTO;
import rs.ac.bg.fon.dtos.User.UserRegistrationDTO;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.utility.ApiResponse;


/**
 * Represents a service layer interface responsible for defining all User and Role related methods.
 * Available API method implementations: GET, POST, PUT, DELETE
 *
 * @author Janko
 * @version 1.0
 */
public interface UserService {

    /**
     * Adds new user to database. Returns instance of saved user from database.
     *
     * @param user instance of User class that is being saved.
     * @return instance of User class that is saved in database,
     * or null if any user field is invalid or if error occurs.
     */
    User saveUser(User user);

    /**
     * Adds new role to database. Returns instance of saved role from database.
     *
     * @param role instance of Role class that is being saved.
     * @return instance of Role class that is saved in database,
     * or null if role already exists or if error occurs.
     */
    Role saveRole(Role role);

    /**
     * Adds new role to user and saves it to database.
     *
     * @param roleName String value of role name that is being added to user.
     * @param username String value of username for user that role is being added to.
     * @return instance of User class representing User that role is added to,
     * or null if error occurs.
     */
    User addRoleToUser(String username, String roleName);

    /**
     * Return User object with username that is specified.
     *
     * @param username String value representing username of User.
     * @return instance of User class.
     */
    User getUser(String username);

    /**
     * Registers user, saves user to database if all checks pass.
     *
     * @param user instance of User class that is being registered.
     * @return instance of User class that is registered,
     * or instance of User class that has invalid fields and is not registered.
     */
    User registerUser(User user);

    /**
     * Deletes User row from database with username that is specified.
     *
     * @param username String value representing username of User.
     * @return instance of User class that is deleted from database.
     */
    User deleteUser(String username);

    /**
     * Updates user data. Returns instance of updated user from database.
     *
     * @param updatedUser instance of User class that contain updated fields.
     * @return instance of User class that is updated in database.
     */
    User updateUser(User updatedUser);

    /**
     * Returns response for API call with messages indicating the success of User deletion from database.
     *
     * @param username String value representing username of User.
     * @return instance of ApiResponse class,
     * containing messages indicating the success of User deletion from the database.
     */
    ApiResponse<?> deleteUserApiResponse(String username);

    /**
     * Transforms UserRegistrationDTO to object of User class and the tries to register that user.
     * Returns response for API call with messages indicating the success of User registration.
     *
     * @param userDTO instance of UserRegistrationDTO class that is attempting to be registered.
     * @return instance of ApiResponse class,
     * containing messages indicating the success of User deletion from the database.
     */
    ApiResponse<?> registerUserApiResponse(UserRegistrationDTO userDTO);

    /**
     * Returns response for API call containing all users for given page.
     *
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing instance of PageDTO object that has an array of UserDTO in it,
     * or error message if operation fails.
     */
    ApiResponse<?> getUsersApiResponse(Pageable pageable);

    /**
     * Returns response for API call containing User object with username that is specified.
     *
     * @param username String value representing username of User.
     * @return instance of ApiResponse class, containing instance of User class.
     */
    ApiResponse<?> getUserApiResponse(String username);

    /**
     * Returns response for API call containing information about adding role to user.
     *
     * @param roleName String value of role name that is being added to user.
     * @param username String value of username for user that role is being added to.
     * @return instance of ApiResponse class, containing messages regarding the success of the operation.
     */
    ApiResponse<?> addRoleToUserApiResponse(String username, String roleName);

    /**
     * Adds new role to database. Returns response for API call containing information about the success of the operation.
     *
     * @param role instance of Role class that is being saved.
     * @return instance of ApiResponse class, containing instance of Role class that is saved,
     * or error message if operation fails.
     */
    ApiResponse<?> saveRoleApiResponse(Role role);

    /**
     * Transforms UserDTO to User class and then updates that User.
     * Returns the updated user and information about the success of the operation.
     *
     * @param user instance of UserDTO class that contain updated fields.
     * @return instance of ApiResponse class, containing instance of User class that is updated in database,
     * or error message if operation fails.
     */
    ApiResponse<?> updateUserApiResponse(UserDTO user);

    /**
     * Returns response for API call containing all users, whose username contains the given string, for given page.
     *
     * @param filterUsername String value representing part of username that search is based on.
     * @param pageable       instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing instance of PageDTO object that has an array of UserDTO in it,
     * or error message if operation fails.
     */
    ApiResponse<?> getFilteredUsersApiResponse(String filterUsername, Pageable pageable);
}
