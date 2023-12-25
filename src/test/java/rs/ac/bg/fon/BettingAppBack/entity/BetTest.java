package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Ticket;

import static org.junit.jupiter.api.Assertions.*;


public class BetTest {

    private Bet bet;

    @BeforeEach
    void setUp(){
        bet = new Bet();
    }
    @AfterEach
    void tearDown() {
        bet = null;
    }

    @Test
    void testBetDefaultValues() {
        assertNull(bet.getId());
        assertNull(bet.getOdd());
        assertNull(bet.getTicket());
        assertEquals(Constants.BET_NOT_FINISHED, bet.getState());
    }

    @Test
    void testBetAllArgsConstructor() {
        Odd odd = new Odd();
        Ticket ticket = new Ticket();
        bet = new Bet(2,Constants.BET_WIN, odd, ticket);

        assertEquals(2, bet.getId());
        assertEquals(odd, bet.getOdd());
        assertEquals(ticket, bet.getTicket());
        assertEquals(Constants.BET_WIN, bet.getState());
    }


    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetBetId(Integer id) {
        bet.setId(id);
        assertEquals(id, bet.getId());
    }
    @Test
    void testSetBetIdThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> bet.setId(null));
        String errorMessage = "Bet ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({Constants.BET_NOT_FINISHED, Constants.BET_LOSS, Constants.BET_WIN})
    void testSetBetState(String state) {
        bet.setState(state);
        assertEquals(state, bet.getState());
    }
    @Test
    void testSetBetStateThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(NullPointerException.class, () -> bet.setState(null));
        String errorMessage = "Bet state can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetBetOdd() {
        Odd odd = new Odd();
        bet.setOdd(odd);
        assertEquals(odd, bet.getOdd());
    }
    @Test
    void testSetBetOddThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(NullPointerException.class, () -> bet.setOdd(null));
        String errorMessage = "Odd can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetTicketForBet() {
        Ticket ticket = new Ticket();
        bet.setTicket(ticket);
        assertEquals(ticket, bet.getTicket());
    }
    @Test
    void testSetTicketForBetThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(NullPointerException.class, () -> bet.setTicket(null));
        String errorMessage = "Ticket can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }


}
