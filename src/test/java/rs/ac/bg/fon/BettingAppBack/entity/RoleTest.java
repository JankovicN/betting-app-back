package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.entity.Role;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @AfterEach
    void tearDown() {
        role = null;
    }

    @Test
    void testRoleDefaultValues() {
        assertNull(role.getId());
        assertNull(role.getName());
    }

    @Test
    void testRoleAllArgsConstructor() {
        role = new Role(2, "ADMIN");

        assertEquals(2, role.getId());
        assertEquals("ADMIN", role.getName());
    }

    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetRoleId(Integer id) {
        role.setId(id);
        assertEquals(id, role.getId());
    }

    @Test
    void testSetRoleIdThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> role.setId(null));
        String errorMessage = "Role ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"ADMIN", "CLIENT", "ENGINEER"})
    void testSetRoleName(String name) {
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    void testSetRoleNameThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> role.setName(null));
        String errorMessage = "Role name can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

}
