package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.OddGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OddTest {

    Odd odd;

    @BeforeEach
    void setUp() {
        odd = new Odd();
    }

    @AfterEach
    void tearDown() {
        odd = null;
    }

    @Test
    void testOddDefaultValues() {
        assertNull(odd.getId());
        assertNull(odd.getName());
        assertNull(odd.getOddValue());
        assertNull(odd.getOddGroup());
        assertNull(odd.getFixture());
        assertEquals(new ArrayList<>(), odd.getBets());
    }

    @Test
    void testOddAllArgsConstructor() {
        Fixture fixture = new Fixture();
        OddGroup oddGroup = new OddGroup();
        List<Bet> bets = new ArrayList<>();
        bets.add(new Bet());
        odd = new Odd(2, BigDecimal.valueOf(1.5), "Home", fixture, oddGroup, bets);

        assertEquals(2, odd.getId());
        assertEquals(BigDecimal.valueOf(1.5), odd.getOddValue());
        assertEquals("Home", odd.getName());
        assertEquals(fixture, odd.getFixture());
        assertEquals(oddGroup, odd.getOddGroup());
        assertEquals(bets, odd.getBets());
    }
    @Test
    void testOddToString() {
        odd.setName("Home");
        odd.setOddValue(BigDecimal.valueOf(1.5));

        assertEquals("Home 1.5", odd.toString());
    }

    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetOddId(Integer id) {
        odd.setId(id);
        assertEquals(id, odd.getId());
    }
    @Test
    void testSetOddIdThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> odd.setId(null));
        String errorMessage = "Odd ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"2.1", "3.6", "1.8", "10.3"})
    void testSetOddValue(BigDecimal oddValue) {
        odd.setOddValue(oddValue);
        assertEquals(oddValue, odd.getOddValue());
    }
    @Test
    void testSetOddValueThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> odd.setOddValue(null));
        String errorMessage = "Odd value can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetOddName() {
        odd.setName("Over 2.5");
        assertEquals("Over 2.5", odd.getName());
    }
    @Test
    void testSetOddNameThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> odd.setName(null));
        String errorMessage = "Odd name can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetOddFixture() {
        Fixture fixture = new Fixture();
        odd.setFixture(fixture);
        assertEquals(fixture, odd.getFixture());
    }
    @Test
    void testSetOddFixtureThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> odd.setFixture(null));
        String errorMessage = "Fixture can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }
    @Test
    void testSetOddGroup() {
        OddGroup oddGroup = new OddGroup();
        odd.setOddGroup(oddGroup);
        assertEquals(oddGroup, odd.getOddGroup());
    }
    @Test
    void testSetOddGroupThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> odd.setOddGroup(null));
        String errorMessage = "Odd Group can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetOddBets() {
        List<Bet> bets = new ArrayList<>();
        bets.add(new Bet());
        odd.setBets(bets);
        assertEquals(bets, odd.getBets());
    }
    @Test
    void testSetOddBetsToNull() {
        odd.setBets(null);
        assertEquals(new ArrayList<>(), odd.getBets());
    }
    @Test
    void testOddBetsAreNotSet() {
        assertEquals(new ArrayList<>(), odd.getBets());
    }
}
