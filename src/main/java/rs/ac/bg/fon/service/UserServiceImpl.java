package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.dtos.Page.PageDTO;
import rs.ac.bg.fon.dtos.User.UserDTO;
import rs.ac.bg.fon.dtos.User.UserRegistrationDTO;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.mappers.PageMapper;
import rs.ac.bg.fon.mappers.UserMapper;
import rs.ac.bg.fon.repository.RoleRepository;
import rs.ac.bg.fon.repository.UserRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.Utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a service layer class responsible for implementing all User and Role related methods.
 * Available API method implementations: GET, POST, PUT, DELETE
 *
 * @author Janko
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    /**
     * Instance of Logger class, responsible for displaying messages that contain information about the success of methods inside User service class.
     */
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    /**
     * Instance of User repository class, responsible for interacting with user table in database.
     */
    private final UserRepository userRepository;

    /**
     * Instance of User repository class, responsible for interacting with user table in database.
     */
    private final RoleRepository roleRepository;

    /**
     * Instance of PasswordEncoder class, responsible for encoding password before it is saved in database.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Instance of Payment service class, responsible for executing any logic related to Payment entity.
     */
    private final PaymentService paymentService;

    /**
     * Finds user for given username and returns his details.
     *
     * @param username String value representing username of user it is loading.
     * @return instance of UserDetails class, containing all data regarding user and his roles.
     * @throws UsernameNotFoundException if user cannot be found by username provided to method
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.info("User found in database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    /**
     * Adds new user to database. Returns instance of saved user from database.
     *
     * @param user instance of User class that is being saved.
     * @return instance of User class that is saved in database,
     *         or null if any user field is invalid or if error occurs.
     *
     */
    @Override
    public User saveUser(User user) {
        try {
            if (user == null
                    || user.getUsername() == null
                    || user.getUsername().isBlank()
                    || user.getPassword() == null
                    || user.getPassword().isBlank()
                    || user.getBirthday() == null
                    || user.getName() == null
                    || user.getName().isBlank()) {
                logger.error("Error while trying to save User, invalid data provided!");
            } else if (user.getBirthday().isAfter(LocalDate.now().minusYears(18))) {
                System.out.println(user.getBirthday());
                logger.error("Error while trying to save User, User must be older than 18!");
                user.setBirthday(null);
            } else {
                log.info("Saving user {}  to database", user.getName());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
        } catch (Exception e) {
            logger.error("Error while trying save User!", e);
        }
        return null;
    }

    /**
     * Adds new role to database. Returns instance of saved role from database.
     *
     * @param role instance of Role class that is being saved.
     * @return instance of Role class that is saved in database,
     *         or null if role already exists or if error occurs.
     *
     */
    @Override
    public Role saveRole(Role role) {
        try {
            log.info("Saving role {} to database", role.getName());
            if (roleRepository.existsByName(role.getName())) {
                logger.error("Role already exists in database!");
                return null;
            }
            return roleRepository.save(role);
        } catch (Exception e) {
            logger.error("Error while trying save Role!", e);
            return null;
        }
    }

    /**
     * Adds new role to user and saves it to database.
     *
     * @param roleName String value of role name that is being added to user.
     * @param username String value of username for user that role is being added to.
     *
     */
    @Override
    public void addRoleToUser(String username, String roleName) {
        try {

            log.info("Adding role {} to user {}", roleName, username);
            User user = userRepository.findByUsername(username);
            Role role = roleRepository.findByName(roleName);
            user.getRoles().add(role);
        } catch (Exception e) {
            logger.error("Error while trying add Role " + roleName + " to User " + username + "!", e);
        }
    }

    /**
     * Return User object with username that is specified.
     *
     * @param username String value representing username of User.
     * @return instance of User class.
     *
     */
    @Transactional
    @Override
    public User getUser(String username) {
        log.info("Fetching  user {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Fetches all users for given page.
     *
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of Page class, containing User objects.
     */
    private Page<User> getUsers(Pageable pageable) {
        log.info("Fetching all users pageable");
        return userRepository.findAll(pageable);
    }

    /**
     * Fetches all users, whose username contains the given string, for given page.
     *
     * @param filterUsername String value representing part of username that search is based on.
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of Page class, containing User objects whose username contains the given string.
     */
    private Page<User> getFilteredUsers(String filterUsername, Pageable pageable) {
        log.info("Fetching users whose username contains \'" + filterUsername + "\' pageable");
        return userRepository.findByUsernameContaining(filterUsername,pageable);
    }


    /**
     * Registers user, saves user to database if all checks pass.
     *
     * @param user instance of User class that is being registered.
     * @return instance of User class that is registered,
     *         or instance of User class that has invalid fields and is not registered.
     *
     */
    @Override
    public User registerUser(User user) {

        if (user == null
                || user.getUsername() == null
                || user.getUsername().isBlank()
                || user.getPassword() == null
                || user.getPassword().isBlank()
                || user.getBirthday() == null
                || user.getName() == null
                || user.getName().isBlank()
                || user.getSurname() == null
                || user.getSurname().isBlank()) {
            logger.error("Error while trying save User, invalid data provided!");
            user.setId(-1);
            return user;
        } else if (user.getBirthday().isAfter(LocalDate.now().minusYears(18))) {
            logger.error("User must be older than 18!");
            user.setBirthday(LocalDate.of(1,1,1));
            return user;
        } else if (!Utility.isValidEmail(user.getEmail())) {
            logger.error("User email is invalid, email = " + user.getEmail() + "!");
            user.setEmail("Invalid Email!");
            return user;
        } else if (userRepository.existsByUsername(user.getUsername())) {
            logger.error("There is already a user with that username!");
            user.setUsername("-1");
            return user;
        } else if (!userRepository.existsByEmail(user.getEmail())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User newUser = userRepository.save(user);
            addRoleToUser(user.getUsername(), "ROLE_CLIENT");
            return newUser;
        } else {
            logger.error("User already registered!");
            return null;
        }
    }

    /**
     * Deletes User row from database with username that is specified.
     *
     * @param username String value representing username of User.
     * @return instance of User class that is deleted from database.
     */
    @Override
    public User deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        userRepository.delete(user);
        return user;
    }

    /**
     * Returns response for API call with messages indicating the success of User deletion from database.
     *
     * @param username String value representing username of User.
     * @return instance of ApiResponse class,
     *         containing messages indicating the success of User deletion from the database.
     *
     */
    @Override
    public ApiResponse<?> deleteUserApiResponse(String username) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            response.setData(deleteUser(username));
            response.addInfoMessage("Successfully deleted user \"" + username + "\".");
        } catch (Exception e) {
            response.addErrorMessage("Unable to delete user at this time, try again later!");
        }
        return response;
    }

    /**
     * Transforms UserRegistrationDTO to object of User class and the tries to register that user.
     * Returns response for API call with messages indicating the success of User registration.
     *
     * @param userDTO instance of UserRegistrationDTO class that is attempting to be registered.
     * @return instance of ApiResponse class,
     *         containing messages indicating the success of User deletion from the database.
     *
     */
    @Override
    public ApiResponse<?> registerUserApiResponse(UserRegistrationDTO userDTO) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            User user = UserMapper.userDtoToUser(userDTO);
            User registeredUser = registerUser(user);
            if (registeredUser == null) {
                response.addErrorMessage("There is already a user registered with that email!");
            } else if (("-1").equals(registeredUser.getUsername())) {
                response.addErrorMessage("There is already a user registered with that username!");
            } else if (LocalDate.of(1,1,1).isEqual(registeredUser.getBirthday())) {
                response.addErrorMessage("User age must be greater than 18 to register!");
            } else if (("Invalid Email!").equals(registeredUser.getEmail())) {
                response.addErrorMessage("Invalid email format!");
            } else if (registeredUser.getId() == -1) {
                response.addErrorMessage("Invalid data for user!");
            } else {
                response.addInfoMessage("Successfully registered!\nWelcome " + user.getUsername() + "!");
            }
        } catch (Exception e) {
            response.addErrorMessage("Unable to register user at this time, try again later!");
        }
        return response;
    }

    /**
     * Returns response for API call containing all users for given page.
     *
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing instance of PageDTO object that has an array of UserDTO in it,
     *         or error message if operation fails.
     */
    @Override
    public ApiResponse<?> getUsersApiResponse(Pageable pageable) {
        ApiResponse<PageDTO<UserDTO>> response = new ApiResponse<>();
        try {
            Page<User> users = getUsers(pageable);
            log.info("All users: " + users.getContent());
            List<UserDTO> userDtosList = users.map(user -> {
                        try {
                            return UserMapper.userToUserDTO(user);
                        } catch (Exception e) {
                            logger.error("Error while mapping user to user DTO");
                            throw null;
                        }
                    }).filter(user -> user != null)
                    .map(userDTO -> {
                        userDTO.setBalance(paymentService.getUserPayments(userDTO.getId()));
                        return userDTO;
                    }).stream().toList();
            Page<UserDTO> userPages = new PageImpl<>(userDtosList, pageable, userDtosList.size());
            PageDTO<UserDTO> pageDTO = PageMapper.pageToPageDTO(userPages);
            response.setData(pageDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Error fetching users: " + e);
            response.addErrorMessage("Unable to get users at this time, try again later!");
        }
        return response;
    }

    /**
     * Returns response for API call containing all users, whose username contains the given string, for given page.
     *
     * @param filterUsername String value representing part of username that search is based on.
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing instance of PageDTO object that has an array of UserDTO in it,
     *         or error message if operation fails.
     */
    @Override
    public ApiResponse<?> getFilteredUsersApiResponse(String filterUsername, Pageable pageable) {
        ApiResponse<PageDTO<UserDTO>> response = new ApiResponse<>();
        try {
            Page<User> users = getFilteredUsers(filterUsername,pageable);
            log.info("Filtered users: " + users.getContent());
            List<UserDTO> userDtosList = users.map(user -> {
                        try {
                            return UserMapper.userToUserDTO(user);
                        } catch (Exception e) {
                            logger.error("Error while mapping user to user DTO");
                            return null;
                        }
                    }).filter(user -> user != null)
                    .map(userDTO -> {
                        userDTO.setBalance(paymentService.getUserPayments(userDTO.getId()));
                        return userDTO;
                    }).stream().toList();
            Page<UserDTO> userPages = new PageImpl<>(userDtosList, pageable, userDtosList.size());
            PageDTO<UserDTO> pageDTO = PageMapper.pageToPageDTO(userPages);
            response.setData(pageDTO);
            response.addInfoMessage("Successfully found users!");
        } catch (Exception e) {
            log.error("Error fetching users: " + e);
            response.addErrorMessage("Unable to get users at this time, try again later!");
        }
        return response;
    }

    /**
     * Returns response for API call containing User object with username that is specified.
     *
     * @param username String value representing username of User.
     * @return instance of ApiResponse class, containing instance of User class.
     *
     */
    @Override
    public ApiResponse<?> getUserApiResponse(String username) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        try {
            User user = getUser(username);
            if (user == null) {
                response.addErrorMessage("User doesn't exist!");
            } else {
                UserDTO userDTO = UserMapper.userToUserDTO(user);
                BigDecimal balance = paymentService.getUserPayments(userDTO.getId());
                userDTO.setBalance(balance);
                response.setData(userDTO);
            }
        } catch (Exception e) {
            log.error("Error fetching user: " + e);
            response.addErrorMessage("Unable to get user at this time, try again later!");
        }
        return response;
    }

    /**
     * Returns response for API call containing information about adding role to user.
     *
     * @param roleName String value of role name that is being added to user.
     * @param username String value of username for user that role is being added to.
     * @return instance of ApiResponse class, containing messages regarding the success of the operation.
     *
     */
    @Override
    public ApiResponse<?> addRoleToUserApiResponse(String username, String roleName) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            addRoleToUser(username, roleName);
            response.addInfoMessage("Successfully added role " + roleName + "to user " + username + "!");
        } catch (Exception e) {
            response.addErrorMessage("Unable to add role " + roleName + " to user " + username + " at this time, try again later!");
        }
        return response;
    }


    /**
     * Adds new role to database. Returns response for API call containing information about the success of the operation.
     *
     * @param role instance of Role class that is being saved.
     * @return instance of ApiResponse class, containing instance of Role class that is saved,
     *         or error message if operation fails.
     *
     */
    @Override
    public ApiResponse<?> saveRoleApiResponse(Role role) {
        ApiResponse<Role> response = new ApiResponse<>();
        try {
            Role savedRole = saveRole(role);
            if (savedRole != null) {
                response.setData(saveRole(savedRole));
                response.addInfoMessage("Successfully added new role " + role.getName() + "!");
                logger.error("Creating successfully Api response for saving new role!");
            } else {
                response.addErrorMessage("Unable to add new role " + role.getName() + " at this time, try again later!");
                logger.error("Error while trying save Role!");
            }
        } catch (Exception e) {
            response.addErrorMessage("Unable to add new role " + role.getName() + " at this time, try again later!");
        }
        return response;
    }

    /**
     * Updates user data. Returns instance of updated user from database.
     *
     * @param updatedUser instance of User class that contain updated fields.
     * @return instance of User class that is updated in database.
     *
     */
    @Override
    public User updateUser(User updatedUser) {
        User user = userRepository.findByUsername(updatedUser.getUsername());
        if (updatedUser.getPassword() == null || updatedUser.getPassword().isBlank()) {
            updatedUser.setPassword(user.getPassword());
        }
        if (updatedUser.getRoles() == null || updatedUser.getRoles().isEmpty()) {
            updatedUser.setRoles(user.getRoles());
        }
        updatedUser.setId(user.getId());
        return userRepository.save(updatedUser);
    }

    /**
     * Transforms UserDTO to User class and then updates that User.
     * Returns the updated user and information about the success of the operation.
     *
     * @param user instance of UserDTO class that contain updated fields.
     * @return instance of ApiResponse class, containing instance of User class that is updated in database,
     *         or error message if operation fails.
     *
     */
    @Override
    public ApiResponse<?> updateUserApiResponse(UserDTO user) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        try {
            if (!Utility.isValidEmail(user.getEmail())) {
                logger.error("User email is invalid, email = " + user.getEmail() + "!");
                response.addErrorMessage("Invalid Email address!");
            }else{
                User userToUpdate = UserMapper.userDtoToUser(user);
                userToUpdate = updateUser(userToUpdate);
                UserDTO userDTO = UserMapper.userToUserDTO(userToUpdate);
                userDTO.setBalance(paymentService.getUserPayments(userDTO.getId()));
                response.setData(userDTO);
                response.addInfoMessage("Successfully updated user " + userToUpdate.getUsername() + "!");
            }
        } catch (Exception e) {
            response.addErrorMessage("Unable to update user " + user.getUsername() + " at this time, try again later!");
        }
        return response;
    }
}
