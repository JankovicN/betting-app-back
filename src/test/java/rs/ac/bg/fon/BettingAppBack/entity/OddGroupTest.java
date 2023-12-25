package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.OddGroup;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OddGroupTest {

    OddGroup oddGroup;
    @BeforeEach
    void setUp(){
        oddGroup = new OddGroup();
    }
    @AfterEach
    void tearDown() {
        oddGroup = null;
    }

    @Test
    void testOddGroupDefaultValues() {
        assertNull(oddGroup.getId());
        assertNull(oddGroup.getName());
        assertEquals(new ArrayList<>(), oddGroup.getOdds());
    }

    @Test
    void testOddGroupAllArgsConstructor() {
        List<Odd> odds = new ArrayList<>();
        odds.add(new Odd());
        oddGroup = new OddGroup(2, "Match Winner", odds);

        assertEquals(2, oddGroup.getId());
        assertEquals("Match Winner", oddGroup.getName());
        assertEquals(odds, oddGroup.getOdds());
    }
    @Test
    void testOddGroupToString() {
        oddGroup.setName("Match Winner");

        assertEquals("Match Winner", oddGroup.toString());
    }

    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetOddGroupId(Integer id) {
        oddGroup.setId(id);
        assertEquals(id, oddGroup.getId());
    }
    @Test
    void testSetOddGroupIdThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> oddGroup.setId(null));
        String errorMessage = "Odd Group ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"Goals Over/Under", "Match Winner", "Both Teams Score"})
    void testSetOddGroupName(String name) {
        oddGroup.setName(name);
        assertEquals(name, oddGroup.getName());
    }
    @Test
    void testSetOddGroupNameThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> oddGroup.setName(null));
        String errorMessage = "Name can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetOddsForOddGroup() {
        List<Odd> odds = new ArrayList<>();
        odds.add(new Odd());
        oddGroup.setOdds(odds);
        assertEquals(odds, oddGroup.getOdds());
    }
    @Test
    void testSetOddsForOddGroupToNull() {
        oddGroup.setOdds(null);
        assertEquals(new ArrayList<>(), oddGroup.getOdds());
    }
    @Test
    void testOddsForOddGroupAreNotSet() {
        assertEquals(new ArrayList<>(), oddGroup.getOdds());
    }
}
