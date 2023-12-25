package rs.ac.bg.fon.BettingAppBack.service.interfaceTest;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.ac.bg.fon.BettingAppBack.util.RoleGenerator;
import rs.ac.bg.fon.BettingAppBack.util.UserDTOGenerator;
import rs.ac.bg.fon.BettingAppBack.util.UserGenerator;
import rs.ac.bg.fon.BettingAppBack.util.UserRegistrationDTOGenerator;
import rs.ac.bg.fon.dtos.Page.PageDTO;
import rs.ac.bg.fon.dtos.User.UserDTO;
import rs.ac.bg.fon.dtos.User.UserRegistrationDTO;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.mappers.UserMapper;
import rs.ac.bg.fon.repository.RoleRepository;
import rs.ac.bg.fon.repository.UserRepository;
import rs.ac.bg.fon.service.PaymentService;
import rs.ac.bg.fon.service.UserService;
import rs.ac.bg.fon.utility.ApiResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public abstract class UserServiceTest {

    protected UserService userService;
    protected UserRepository userRepository;
    protected RoleRepository roleRepository;
    protected PasswordEncoder passwordEncoder;
    protected PaymentService paymentService;


    // Test save User
    @Test
    void testSaveUserSuccess() {
        User user = UserGenerator.generateUniqueUser();
        user.setBirthday(LocalDate.now().minusYears(19));

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertEquals(user, savedUser);
    }

    @Test
    void testSaveUserMissingUsername() {
        User user = UserGenerator.generateUniqueUser();
        user.setUsername("");
        User savedUser = userService.saveUser(user);

        assertNull(savedUser);
    }

    @Test
    void testSaveUserMissingPassword() {
        User user = UserGenerator.generateUniqueUser();
        user.setPassword("");
        User savedUser = userService.saveUser(user);

        assertNull(savedUser);
    }

    @Test
    void testSaveUserMissingName() {
        User user = UserGenerator.generateUniqueUser();
        user.setName("");
        User savedUser = userService.saveUser(user);

        assertNull(savedUser);
    }

    @Test
    void testSaveUserInvalidBirthday() {
        User user = UserGenerator.generateUniqueUser();

        User savedUser = userService.saveUser(user);

        assertNull(savedUser);
    }

    @Test
    void testSaveUserDatabaseError() {
        User user = UserGenerator.generateUniqueUser();

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Simulated database error!"));

        User savedUser = userService.saveUser(user);
        assertNull(savedUser);
    }


    // Test save Role
    @Test
    void testSaveRoleSuccess() {
        Role role = new Role(1, "role");

        when(roleRepository.existsByName(role.getName())).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(role);

        Role savedRole = userService.saveRole(role);

        assertEquals(role, savedRole);
    }

    @Test
    void testSaveRoleRoleAlreadyExists() {
        Role role = new Role(1, "role");

        when(roleRepository.existsByName(role.getName())).thenReturn(true);

        Role savedRole = userService.saveRole(role);

        assertNull(savedRole);
    }

    @Test
    void testSaveRoleRoleDatabaseError() {
        Role role = new Role(1, "role");

        when(roleRepository.existsByName(role.getName())).thenReturn(false);
        when(roleRepository.save(role)).thenThrow(new RuntimeException("Simulated database error!"));

        Role savedRole = userService.saveRole(role);

        assertNull(savedRole);
    }


    // Test add Role to User
    @Test
    void testAddRoleToUserSuccess() {
        User user = UserGenerator.generateUniqueUser();
        Role role = RoleGenerator.getAdminRole();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(roleRepository.findByName(role.getName())).thenReturn(role);

        User updatedUser = userService.addRoleToUser(user.getUsername(), role.getName());

        assertEquals(role.getName(), updatedUser.getRoles().iterator().next().getName());
        assertEquals(1, user.getRoles().size());
        assertEquals(role, user.getRoles().toArray()[0]);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(roleRepository, times(1)).findByName(role.getName());
    }

    @Test
    void testAddRoleToUserCannotFindUser() {
        User user = UserGenerator.generateUniqueUser();
        Role role = RoleGenerator.getAdminRole();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(roleRepository.findByName(role.getName())).thenReturn(role);

        User updatedUser = userService.addRoleToUser(user.getUsername(), role.getName());

        assertNull(updatedUser);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(roleRepository, times(1

        )).findByName(role.getName());
    }

    @Test
    void testAddRoleToUserDatabaseErrorForUser() {
        User user = UserGenerator.generateUniqueUser();
        Role role = RoleGenerator.getAdminRole();
        when(userRepository.findByUsername(user.getUsername())).thenThrow(new RuntimeException("Simulated exception"));

        User updatedUser = userService.addRoleToUser(user.getUsername(), role.getName());

        assertNull(updatedUser);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(roleRepository, times(0)).findByName(role.getName());
    }

    @Test
    void testAddRoleToUserDatabaseErrorForRole() {
        User user = UserGenerator.generateUniqueUser();
        Role role = RoleGenerator.getAdminRole();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(roleRepository.findByName(role.getName())).thenThrow(new RuntimeException("Simulated exception"));

        User updatedUser = userService.addRoleToUser(user.getUsername(), role.getName());

        assertNull(updatedUser);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(roleRepository, times(1)).findByName(role.getName());
    }

    // Test get User by Username
    @Test
    void testGetUserSuccess() {
        User user = UserGenerator.generateUniqueUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        User foundUser = userService.getUser(user.getUsername());
        assertEquals(user, foundUser);
    }

    @Test
    void testGetUserNoUserFound() {

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        User foundUser = userService.getUser("username");
        assertNull(foundUser);
    }


    // Test register User

    @Test
    void testRegisterUserSuccess() {
        User user = UserGenerator.generateUniqueUser();
        user.setBirthday(LocalDate.now().minusYears(19));
        user.setEmail("user@email.com");

        User encodedUser = user;
        encodedUser.setPassword("hashedPassword");

        Role role = new Role(1, "ROLE_CLIENT");

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(encodedUser);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(encodedUser);

        when(roleRepository.findByName(role.getName())).thenReturn(role);

        encodedUser.getRoles().add(role);

        User savedUser = userService.registerUser(user);

        assertEquals(encodedUser, savedUser);
    }

    @Test
    void testRegisterUserMissingUsername() {
        User user = UserGenerator.generateUniqueUser();
        user.setUsername("");

        User savedUser = userService.registerUser(user);

        assertEquals(-1, savedUser.getId());
    }

    @Test
    void testRegisterUserMissingPassword() {
        User user = UserGenerator.generateUniqueUser();
        user.setPassword("");

        User savedUser = userService.registerUser(user);

        assertEquals(-1, savedUser.getId());
    }

    @Test
    void testRegisterUserMissingName() {
        User user = UserGenerator.generateUniqueUser();
        user.setName("");

        User savedUser = userService.registerUser(user);

        assertEquals(-1, savedUser.getId());
    }

    @Test
    void testRegisterUserInvalidBirthday() {
        User user = UserGenerator.generateUniqueUser();
        user.setBirthday(LocalDate.now());

        User savedUser = userService.registerUser(user);

        assertEquals(LocalDate.of(1, 1, 1), savedUser.getBirthday());
    }

    @Test
    void testRegisterUserInvalidEmail() {
        User user = UserGenerator.generateUniqueUser();
        user.setEmail("email");

        User savedUser = userService.registerUser(user);

        assertEquals("Invalid Email!", savedUser.getEmail());
    }

    @Test
    void testRegisterUserTakenUsername() {
        User user = UserGenerator.generateUniqueUser();

        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        User savedUser = userService.registerUser(user);

        assertEquals("-1", savedUser.getUsername());
    }

    @Test
    void testRegisterUserTakenEmail() {
        User user = UserGenerator.generateUniqueUser();

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        User savedUser = userService.registerUser(user);

        assertEquals("Email already registered!", savedUser.getEmail());
    }


    // Test delete User
    @Test
    void testDeleteUserSuccess() {

        User user = UserGenerator.generateUniqueUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        doNothing().when(userRepository).delete(user);

        User deletedUser = userService.deleteUser(user.getUsername());
        assertEquals(user, deletedUser);
    }

    @Test
    void testDeleteUserCannotFindUser() {

        User user = UserGenerator.generateUniqueUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        User deletedUser = userService.deleteUser(user.getUsername());
        assertNull(deletedUser);
    }

    @Test
    void testDeleteUserErrorDeletingUser() {

        User user = UserGenerator.generateUniqueUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        User deletedUser = userService.deleteUser(user.getUsername());
        assertNull(deletedUser);
    }

    // Test delete User API Response

    @Test
    void testDeleteUserApiResponseSuccess() {

        User user = UserGenerator.generateUniqueUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        doNothing().when(userRepository).delete(user);

        ApiResponse<?> apiResponse = userService.deleteUserApiResponse(user.getUsername());
        assertEquals(user, apiResponse.getData());
        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully deleted user \"" + user.getUsername() + "\".", apiResponse.getInfoMessages().get(0));
    }

    @Test
    void testDeleteUserApiResponseCannotFindUser() {

        User user = UserGenerator.generateUniqueUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        ApiResponse<?> apiResponse = userService.deleteUserApiResponse(user.getUsername());
        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("User with username " + user.getUsername() + " doesn't exist!", apiResponse.getErrorMessages().get(0));
    }


    // Test register User API Response
    @Test
    void testRegisterUserApiResponseSuccess() throws Exception {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOGenerator.generateUniqueUserDTO();
        User user = UserMapper.userDtoToUser(userRegistrationDTO);
        user.setId(1);

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded_password");
        user.setPassword("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Role role = new Role(1, "ROLE_CLIENT");
        user.getRoles().add(role);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        when(roleRepository.findByName(role.getName())).thenReturn(role);

        ApiResponse<?> apiResponse = userService.registerUserApiResponse(userRegistrationDTO);

        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully registered!\nWelcome " + user.getUsername() + "!", apiResponse.getInfoMessages().get(0));
        assertFalse(user.getRoles().isEmpty());
        assertEquals("ROLE_CLIENT", user.getRoles().iterator().next().getName());
        assertEquals("encoded_password", user.getPassword());
    }

    @Test
    void testRegisterUserApiResponseUserAlreadyExists() throws Exception {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOGenerator.generateUniqueUserDTO();
        User user = UserMapper.userDtoToUser(userRegistrationDTO);
        user.setId(1);

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded_password");
        user.setPassword("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Role role = new Role(1, "ROLE_CLIENT");
        user.getRoles().add(role);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        when(roleRepository.findByName(role.getName())).thenReturn(role);

        ApiResponse<?> apiResponse = userService.registerUserApiResponse(userRegistrationDTO);

        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully registered!\nWelcome " + user.getUsername() + "!", apiResponse.getInfoMessages().get(0));
        assertFalse(user.getRoles().isEmpty());
        assertEquals("ROLE_CLIENT", user.getRoles().iterator().next().getName());
        assertEquals("encoded_password", user.getPassword());
    }


    @Test
    void testRegisterApiResponseUserMissingUsername() {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOGenerator.generateUniqueUserDTO();
        userRegistrationDTO.setUsername("");

        ApiResponse<?> apiResponse = userService.registerUserApiResponse(userRegistrationDTO);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Unable to register user at this time, try again later!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testRegisterUserApiResponseInvalidBirthday() {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOGenerator.generateUniqueUserDTO();
        userRegistrationDTO.setBirthday("2023-01-01");

        ApiResponse<?> apiResponse = userService.registerUserApiResponse(userRegistrationDTO);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("User age must be greater than 18 to register!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testRegisterUserApiResponseInvalidEmail() {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOGenerator.generateUniqueUserDTO();
        userRegistrationDTO.setEmail("email");

        ApiResponse<?> apiResponse = userService.registerUserApiResponse(userRegistrationDTO);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Invalid email format!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testRegisterUserApiResponseTakenUsername() {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOGenerator.generateUniqueUserDTO();

        when(userRepository.existsByUsername(userRegistrationDTO.getUsername())).thenReturn(true);

        ApiResponse<?> apiResponse = userService.registerUserApiResponse(userRegistrationDTO);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("There is already a user registered with that username!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testRegisterUserApiResponseTakenEmail() {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOGenerator.generateUniqueUserDTO();

        when(userRepository.existsByUsername(userRegistrationDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userRegistrationDTO.getEmail())).thenReturn(true);

        ApiResponse<?> apiResponse = userService.registerUserApiResponse(userRegistrationDTO);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("There is already a user registered with that email!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testRegisterUserApiResponseErrorRegistering() {

        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTOGenerator.generateUniqueUserDTO();

        when(userRepository.existsByUsername(userRegistrationDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userRegistrationDTO.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(anyString())).thenThrow(new RuntimeException("Simulated error!"));

        ApiResponse<?> apiResponse = userService.registerUserApiResponse(userRegistrationDTO);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error registering user!", apiResponse.getErrorMessages().get(0));
    }

    // Test get Users by Page API Response
    @Test
    void testGetUsersApiResponseSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<User> userList = UserGenerator.generateUniqueUsers(2);
        Page<User> page = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findAll(pageable)).thenReturn(page);

        when(paymentService.getUserPayments(anyInt())).thenReturn(BigDecimal.valueOf(1000.0));

        ApiResponse<?> apiResponse = userService.getUsersApiResponse(pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(UserDTO.class, ((PageDTO) apiResponse.getData()).getContent().get(0).getClass());
        assertEquals(2, ((PageDTO) apiResponse.getData()).getContent().size());
    }

    @Test
    void testGetUsersApiResponseInvalidUser() {
        Pageable pageable = Pageable.unpaged();
        List<User> userList = UserGenerator.generateUniqueUsers(2);
        userList.get(1).setEmail("");
        Page<User> page = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findAll(pageable)).thenReturn(page);

        when(paymentService.getUserPayments(anyInt())).thenReturn(BigDecimal.valueOf(1000.0));

        ApiResponse<?> apiResponse = userService.getUsersApiResponse(pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(UserDTO.class, ((PageDTO) apiResponse.getData()).getContent().get(0).getClass());
        assertEquals(1, ((PageDTO) apiResponse.getData()).getContent().size());
    }

    @Test
    void testGetUsersApiResponseErrorIsThrown() {
        Pageable pageable = Pageable.unpaged();
        List<User> userList = UserGenerator.generateUniqueUsers(2);
        userList.get(1).setEmail("");
        userList.get(0).setEmail("");
        Page<User> page = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findAll(pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = userService.getUsersApiResponse(pageable);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Unable to get users at this time, try again later!", apiResponse.getErrorMessages().get(0));
    }


    // Test get Users by Page and filter by Username API Response
    @Test
    void testGetFilteredUsersApiResponseSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<User> userList = UserGenerator.generateUniqueUsers(2);
        Page<User> page = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findByUsernameContaining("user", pageable)).thenReturn(page);

        when(paymentService.getUserPayments(anyInt())).thenReturn(BigDecimal.valueOf(1000.0));

        ApiResponse<?> apiResponse = userService.getFilteredUsersApiResponse("user", pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(UserDTO.class, ((PageDTO) apiResponse.getData()).getContent().get(0).getClass());
        assertEquals(2, ((PageDTO) apiResponse.getData()).getContent().size());
        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully found users!", apiResponse.getInfoMessages().get(0));
    }

    @Test
    void testGetFilteredUsersApiResponseInvalidUser() {
        Pageable pageable = Pageable.unpaged();
        List<User> userList = UserGenerator.generateUniqueUsers(2);
        userList.get(1).setEmail("");
        Page<User> page = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findByUsernameContaining("user", pageable)).thenReturn(page);

        when(paymentService.getUserPayments(anyInt())).thenReturn(BigDecimal.valueOf(1000.0));

        ApiResponse<?> apiResponse = userService.getFilteredUsersApiResponse("user", pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(UserDTO.class, ((PageDTO) apiResponse.getData()).getContent().get(0).getClass());
        assertEquals(1, ((PageDTO) apiResponse.getData()).getContent().size());
        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully found users!", apiResponse.getInfoMessages().get(0));
    }

    @Test
    void testGetFilteredUsersApiResponseErrorIsThrown() {
        Pageable pageable = Pageable.unpaged();
        List<User> userList = UserGenerator.generateUniqueUsers(2);
        userList.get(1).setEmail("");
        userList.get(0).setEmail("");
        Page<User> page = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findByUsernameContaining("user", pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = userService.getFilteredUsersApiResponse("user", pageable);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Unable to get users at this time, try again later!", apiResponse.getErrorMessages().get(0));
    }


    // Test get User by Username API Response
    @Test
    void testGetUserApiResponseSuccess() {
        User user = UserGenerator.generateUniqueUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        when(paymentService.getUserPayments(anyInt())).thenReturn(BigDecimal.valueOf(1000.0));

        ApiResponse<?> apiResponse = userService.getUserApiResponse(user.getUsername());

        assertNotNull(apiResponse.getData());
        assertEquals(UserDTO.class, apiResponse.getData().getClass());
        assertEquals(user.getUsername(), ((UserDTO) apiResponse.getData()).getUsername());
    }

    @Test
    void testGetUserApiResponseError() {
        User user = UserGenerator.generateUniqueUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        when(paymentService.getUserPayments(anyInt())).thenThrow(new RuntimeException("SimulatedError"));

        ApiResponse<?> apiResponse = userService.getUserApiResponse(user.getUsername());

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Unable to get user at this time, try again later!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testGetUserApiResponseNoUserFound() {

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        ApiResponse<?> apiResponse = userService.getUserApiResponse("username");

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("User doesn't exist!", apiResponse.getErrorMessages().get(0));
    }


    // Test add Role to User API Response
    @Test
    void testAddRoleToUserApiResponseSuccess() {
        User user = UserGenerator.generateUniqueUser();
        Role role = RoleGenerator.getAdminRole();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(roleRepository.findByName(role.getName())).thenReturn(role);

        ApiResponse<?> apiResponse = userService.addRoleToUserApiResponse(user.getUsername(), role.getName());

        assertEquals("Successfully added role " + role.getName() + "to user " + user.getUsername() + "!", apiResponse.getInfoMessages().get(0));

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(roleRepository, times(1)).findByName(role.getName());
    }

    @Test
    void testAddRoleToUserApiResponseCannotFindUser() {
        User user = UserGenerator.generateUniqueUser();
        Role role = RoleGenerator.getAdminRole();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(roleRepository.findByName(role.getName())).thenReturn(role);


        ApiResponse<?> apiResponse = userService.addRoleToUserApiResponse(user.getUsername(), role.getName());

        assertEquals("Unable to add role " + role.getName() + " to user " + user.getUsername() + " at this time, try again later!", apiResponse.getErrorMessages().get(0));

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(roleRepository, times(1)).findByName(role.getName());
    }


    // Test save Role API Response
    @Test
    void testSaveRoleApiResponseSuccess() {
        Role role = new Role(1, "role");

        when(roleRepository.existsByName(role.getName())).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(role);

        ApiResponse<?> apiResponse = userService.saveRoleApiResponse(role);

        assertEquals(role, apiResponse.getData());
        assertEquals("Successfully added new role " + role.getName() + "!", apiResponse.getInfoMessages().get(0));
        assertEquals(1, apiResponse.getInfoMessages().size());
    }

    @Test
    void testSaveRoleApiResponseRoleAlreadyExists() {
        Role role = new Role(1, "role");

        when(roleRepository.existsByName(role.getName())).thenReturn(true);

        ApiResponse<?> apiResponse = userService.saveRoleApiResponse(role);

        assertEquals("Unable to add new role " + role.getName() + " at this time, try again later!", apiResponse.getErrorMessages().get(0));
    }


    // Test update User
    @Test
    void testUpdateUserSuccess() {
        User user = UserGenerator.generateUniqueUser();
        User updatedUser = UserGenerator.generateUniqueUser();
        updatedUser.getRoles().add(new Role(1, "role"));

        when(userRepository.findByUsername(updatedUser.getUsername())).thenReturn(user);
        updatedUser.setId(user.getId());
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User savedUser = userService.updateUser(updatedUser);

        assertEquals(updatedUser, savedUser);
        assertEquals(user.getId(), savedUser.getId());
    }

    @Test
    void testUpdateUserError() {
        User user = UserGenerator.generateUniqueUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        User savedUser = userService.updateUser(user);

        assertNull(savedUser);
    }


    // Test update User API Response
    @Test
    void testUpdateUserApiResponseSuccess() throws Exception {
        UserDTO userDTO = UserDTOGenerator.generateUniqueUserDTO();
        User user = UserGenerator.generateUniqueUser();
        User updatedUser = UserMapper.userDtoToUser(userDTO);
        updatedUser.getRoles().add(new Role(1, "role"));

        when(userRepository.findByUsername(updatedUser.getUsername())).thenReturn(user);
        updatedUser.setId(user.getId());
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        when(paymentService.getUserPayments(anyInt())).thenReturn(BigDecimal.valueOf(1000.0));

        UserDTO updatedUserDTO = UserMapper.userToUserDTO(updatedUser);

        ApiResponse<?> apiResponse = userService.updateUserApiResponse(userDTO);

        assertEquals(UserDTO.class, apiResponse.getData().getClass());
        assertEquals(updatedUserDTO.getUsername(), ((UserDTO) apiResponse.getData()).getUsername());
        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully updated user " + updatedUserDTO.getUsername() + "!", apiResponse.getInfoMessages().get(0));
    }

    @Test
    void testUpdateUserApiResponseInvalidEmail() {
        UserDTO userDTO = UserDTOGenerator.generateUniqueUserDTO();
        userDTO.setEmail("email");

        ApiResponse<?> apiResponse = userService.updateUserApiResponse(userDTO);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Invalid Email address!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testUpdateUserApiResponseErrorUpdating() throws Exception {
        UserDTO userDTO = UserDTOGenerator.generateUniqueUserDTO();
        User user = UserGenerator.generateUniqueUser();
        User updatedUser = UserMapper.userDtoToUser(userDTO);
        updatedUser.getRoles().add(new Role(1, "role"));

        when(userRepository.findByUsername(updatedUser.getUsername())).thenThrow(new RuntimeException("Simulated exception!"));

        ApiResponse<?> apiResponse = userService.updateUserApiResponse(userDTO);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Unable to update user " + userDTO.getUsername() + " at this time, try again later!", apiResponse.getErrorMessages().get(0));
    }

}
