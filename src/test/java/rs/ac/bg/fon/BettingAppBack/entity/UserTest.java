package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    void testUserDefaultValues() {
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getSurname());
        assertNull(user.getEmail());
        assertNull(user.getBirthday());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertEquals(new ArrayList<>(), user.getRoles());
    }

    @Test
    void testUserAllArgsConstructor() {
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role());
        LocalDate birthday = LocalDate.now();
        user = new User(2, "Ime", "Prezime", "mojMejl@student.fon.bg.ac.rs", birthday, "username", "password", roles);

        assertEquals(2, user.getId());
        assertEquals("Ime", user.getName());
        assertEquals("Prezime", user.getSurname());
        assertEquals("mojMejl@student.fon.bg.ac.rs", user.getEmail());
        assertEquals(birthday, user.getBirthday());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(roles, user.getRoles());
    }

    @Test
    void testUserToString() {
        user.setName("Marko");
        user.setSurname("Markovic");

        assertEquals("Marko Markovic", user.toString());
    }

    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetUserId(Integer id) {
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    void testSetUserIdThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> user.setId(null));
        String errorMessage = "User ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetUserName() {
        user.setName("Petar");
        assertEquals("Petar", user.getName());
    }

    @Test
    void testSetUserNameThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> user.setName(null));
        String errorMessage = "Name of user can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetUserSurname() {
        user.setSurname("Petrovic");
        assertEquals("Petrovic", user.getSurname());
    }

    @Test
    void testSetUserSurnameThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> user.setSurname(null));
        String errorMessage = "Surname of user can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetUserEmail() {
        user.setEmail("mejl@gmail.com");
        assertEquals("mejl@gmail.com", user.getEmail());
    }

    @Test
    void testSetUserEmailThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> user.setEmail(null));
        String errorMessage = "Email can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetUserBirthday() {
        LocalDate birthday = LocalDate.now();
        user.setBirthday(birthday);
        assertEquals(birthday, user.getBirthday());
    }

    @Test
    void testSetUserBirthdayThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> user.setBirthday(null));
        String errorMessage = "User birthday can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetUserUsername() {
        user.setUsername("username");
        assertEquals("username", user.getUsername());
    }

    @Test
    void testSetUserUsernameThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> user.setUsername(null));
        String errorMessage = "Username can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetUserPassword() {
        user.setPassword("secret password");
        assertEquals("secret password", user.getPassword());
    }

    @Test
    void testSetUserPasswordThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> user.setPassword(null));
        String errorMessage = "Password can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetUserRoles() {
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role());
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }

    @Test
    void testUserRolesNotSet() {
        assertEquals(new ArrayList<>(), user.getRoles());
    }

    @Test
    void testSetUserRolesThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> user.setRoles(null));
        String errorMessage = "User roles can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }
}
