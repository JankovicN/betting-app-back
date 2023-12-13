package rs.ac.bg.fon.BettingAppBack.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {


    Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
    }

    @AfterEach
    void tearDown() {
        ticket = null;
    }

    @Test
    void testTicketDefaultValues() {
        assertNull(ticket.getId());
        assertNull(ticket.getWager());
        assertNull(ticket.getTotalWin());
        assertNull(ticket.getDate());
        assertNull(ticket.getState());
        assertNull(ticket.getUser());
        assertEquals(0.0, ticket.getOdd());
        assertEquals(new ArrayList<>(), ticket.getBets());
    }

    @Test
    void testTicketAllArgsConstructor() {
        LocalDateTime date = LocalDateTime.now();
        List<Bet> bets = new ArrayList<>();
        bets.add(new Bet());
        User user = new User();
        ticket = new Ticket(2, BigDecimal.valueOf(200.0), 10.0, BigDecimal.valueOf(2000.0), date, Constants.TICKET_PROCESSED, bets, user);

        assertEquals(2, ticket.getId());
        assertEquals(BigDecimal.valueOf(200.0), ticket.getWager());
        assertEquals(10.0, ticket.getOdd());
        assertEquals(BigDecimal.valueOf(2000.0), ticket.getTotalWin());
        assertEquals(date, ticket.getDate());
        assertEquals(Constants.TICKET_PROCESSED, ticket.getState());
        assertEquals(bets, ticket.getBets());
        assertEquals(user, ticket.getUser());
    }

    @ParameterizedTest
    @CsvSource({"2", "3", "234", "6"})
    void testSetTicketId(Integer id) {
        ticket.setId(id);
        assertEquals(id, ticket.getId());
    }

    @Test
    void testSetTicketIdThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> ticket.setId(null));
        String errorMessage = "Ticket ID can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"20.0", "320.0", "234.565", "231"})
    void testSetTicketWager(BigDecimal wager) {
        ticket.setWager(wager);
        assertEquals(wager, ticket.getWager());
    }

    @Test
    void testSetTicketWagerThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> ticket.setWager(null));
        String errorMessage = "Wager value can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"20.0", "320.0", "234.565", "231"})
    void testSetTicketOdd(Double odd) {
        ticket.setOdd(odd);
        assertEquals(odd, ticket.getOdd());
    }

    @ParameterizedTest
    @CsvSource({"20.0", "320.0", "234.565", "231"})
    void testSetTicketTotalWin(BigDecimal totalWin) {
        ticket.setTotalWin(totalWin);
        assertEquals(totalWin, ticket.getTotalWin());
    }

    @Test
    void testSetTicketTotalWinThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> ticket.setTotalWin(null));
        String errorMessage = "Total win value can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetTicketDate() {
        LocalDateTime now = LocalDateTime.now();
        ticket.setDate(now);
        assertEquals(now, ticket.getDate());
    }

    @Test
    void testSetTicketDateThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> ticket.setDate(null));
        String errorMessage = "Ticket date can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({Constants.TICKET_UNPROCESSED, Constants.TICKET_PROCESSED, Constants.TICKET_WIN,
            Constants.TICKET_LOSS, Constants.TICKET_CANCELED, Constants.TICKET_PAYOUT})
    void testSetTicketState(String state) {
        ticket.setState(state);
        assertEquals(state, ticket.getState());
    }

    @Test
    void testSetTicketStateThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> ticket.setState(null));
        String errorMessage = "Ticket state can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }

    @Test
    void testSetTicketBets() {
        List<Bet> bets = new ArrayList<>();
        bets.add(new Bet());
        ticket.setBets(bets);
        assertEquals(bets, ticket.getBets());
    }

    @Test
    void testSetTicketBetsToNull() {
        ticket.setBets(null);
        assertEquals(new ArrayList<>(), ticket.getBets());
    }

    @Test
    void testTicketBetsAreNotSet() {
        assertEquals(new ArrayList<>(), ticket.getBets());
    }

    @Test
    void testSetTicketUser() {
        User user = new User();
        ticket.setUser(user);
        assertEquals(user, ticket.getUser());
    }
    @Test
    void testSetTicketUserThrowsNullPointerException() {
        NullPointerException nullPointerException
                = assertThrows(java.lang.NullPointerException.class, () -> ticket.setUser(null));
        String errorMessage = "Ticket user can not be null!";
        assertEquals(errorMessage, nullPointerException.getMessage());
    }
}
