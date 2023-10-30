package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.dtos.User.UserDTO;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.mappers.UserMapper;
import rs.ac.bg.fon.repository.RoleRepository;
import rs.ac.bg.fon.repository.UserRepository;
import rs.ac.bg.fon.utility.ApiResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    final PaymentService paymentService;

    private final UserMapper userMapper;

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
                logger.error("Error while trying save User, invalid data provided!");
            } else if (user.getBirthday().isBefore(LocalDate.now().minusYears(18))) {
                logger.error("Error while trying save User, User must be older than 18!");
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

    @Override
    public Role saveRole(Role role) {
        try {
            log.info("Saving role {} to database", role.getName());
            if (roleRepository.existsByName(role.getName())) {
                logger.error("Error while trying save Role!");
                return null;
            }
            return roleRepository.save(role);
        } catch (Exception e) {
            logger.error("Error while trying save Role!", e);
        }
        return null;
    }

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

    @Transactional
    @Override
    public User getUser(String username) {
        log.info("Fetcing  user {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUser(Integer userId) {
        log.info("Fetcing  user {}", userId);
        return userRepository.findById(userId).get();
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

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
        } else if (user.getBirthday().isBefore(LocalDate.now().minusYears(18))) {
            logger.error("User must be older than 18!");
            user.setBirthday(null);
        }
        if (!userRepository.existsByEmail(user.getEmail())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println(user.getPassword());
            userRepository.save(user);
            addRoleToUser(user.getUsername(), "ROLE_CLIENT");
        }
        return null;
    }

    @Override
    public User deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        userRepository.delete(user);
        return user;
    }


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

    @Override
    public ApiResponse<?> registerUserApiResponse(User user) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            response.setData(registerUser(user));
            response.addInfoMessage("Successfully registered!\nWelcome " + user.getUsername() + "!");
        } catch (Exception e) {
            response.addErrorMessage("Unable to register user at this time, try again later!");
        }
        return response;
    }

    @Override
    public ApiResponse<?> getUsersApiResponse() {
        ApiResponse<List<UserDTO>> response = new ApiResponse<>();
        try {
            List<User> users = getUsers();
            log.info("All users: " + users.toString());
            List<UserDTO> userDTOS = userMapper.userToUserDTO(users);
            for (UserDTO dto : userDTOS) {
                BigDecimal balance = paymentService.getUserPayments(dto.getId());
                dto.setBalance(balance);
            }
            response.setData(userDTOS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Error fetching users: " + e);
            response.addErrorMessage("Unable to get users at this time, try again later!");
        }
        return response;
    }

    @Override
    public ApiResponse<?> getUserApiResponse(String username) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        try {
            UserDTO userDTO = userMapper.userToUserDTO(getUser(username));
            BigDecimal balance = paymentService.getUserPayments(userDTO.getId());
            userDTO.setBalance(balance);
            response.setData(userDTO);
        } catch (Exception e) {
            log.error("Error fetching user: " + e);
            response.addErrorMessage("Unable to get user at this time, try again later!");
        }
        return response;
    }

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

    @Override
    public User updateUser(User updatedUser) {
        User user = userRepository.findByUsername(updatedUser.getUsername());
        updatedUser.setId(user.getId());
        return userRepository.save(updatedUser);
    }

    @Override
    public ApiResponse<?> updateUserApiResponse(User user) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            response.setData(updateUser(user));
            response.addInfoMessage("Successfully updated user " + user.getUsername() + "!");
        } catch (Exception e) {
            response.addErrorMessage("Unable to update user " + user.getUsername() + " at this time, try again later!");
        }
        return response;
    }
}
