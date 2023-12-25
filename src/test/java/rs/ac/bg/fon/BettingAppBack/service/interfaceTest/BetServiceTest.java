package rs.ac.bg.fon.BettingAppBack.service.interfaceTest;

import org.junit.jupiter.api.Test;
import rs.ac.bg.fon.BettingAppBack.util.BetGenerator;
import rs.ac.bg.fon.BettingAppBack.util.OddGenerator;
import rs.ac.bg.fon.BettingAppBack.util.OddGroupGenerator;
import rs.ac.bg.fon.dtos.Bet.BetInfoDTO;
import rs.ac.bg.fon.entity.*;
import rs.ac.bg.fon.mappers.BetMapper;
import rs.ac.bg.fon.repository.BetRepository;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public abstract class BetServiceTest {

    protected BetRepository betRepository;

    protected BetService betService;

    @Test
    void testSaveBetSuccess() {
        Bet bet = BetGenerator.generateUniqueBet();
        when(betRepository.save(bet)).thenReturn(bet);

        Bet savedBet = betService.save(bet);
        assertEquals(savedBet, bet);
    }

    @Test
    void testSaveBetShouldReturnNull() {
        when(betRepository.save(null)).thenThrow(new NullPointerException());

        Bet savedBet = betService.save(null);
        assertNull(savedBet);
    }


    @Test
    void testUpdateAllBetsSuccess() throws Exception {
        doNothing().when(betRepository).updateAllBets();

        betService.updateAllBets();
        verify(betRepository, times(1)).updateAllBets();
    }

    @Test()
    void testUpdateAllBetsFailure() {
        doThrow(new RuntimeException("Simulated database error")).when(betRepository).updateAllBets();

        Exception exception
                = assertThrows(Exception.class, () -> betService.updateAllBets());
        assertEquals("Error while trying to update Bets!\nSimulated database error", exception.getMessage());
    }

    @Test
    void testSaveBetsForTicketSuccess() throws Exception {
        Bet bet1 = new Bet();
        bet1.setOdd(new Odd());
        Bet bet2 = new Bet();
        bet2.setOdd(new Odd());
        List<Bet> betList = new ArrayList<>();
        betList.add(bet1);
        betList.add(bet2);
        Ticket ticket = new Ticket();
        ticket.setBets(betList);

        betService.saveBetsForTicket(betList, ticket);

        verify(betRepository, times(betList.size())).save(any(Bet.class));
    }

    @Test()
    void testSaveBetsForTicketFailure() {

        Bet bet1 = new Bet();
        bet1.setOdd(new Odd());
        List<Bet> betList = new ArrayList<>();
        betList.add(bet1);
        Ticket ticket = new Ticket();
        ticket.setBets(betList);

        doThrow(new RuntimeException("Simulated database error")).when(betRepository).save(any(Bet.class));

        Exception exception
                = assertThrows(Exception.class, () -> betService.saveBetsForTicket(betList, ticket));
        assertEquals("Unable to save all Bets for Ticket!", exception.getMessage());
    }

    @Test()
    void testSaveBetsForTicketNullBet() {

        List<Bet> betList = new ArrayList<>();
        betList.add(null);
        Ticket ticket = new Ticket();
        ticket.setBets(betList);

        Exception exception
                = assertThrows(Exception.class, () -> betService.saveBetsForTicket(betList, ticket));
        assertEquals("Unable to save all Bets for Ticket!", exception.getMessage());
    }

    @Test()
    void testSaveBetsForTicketNullBetOdd() {

        Bet bet1 = new Bet();
        List<Bet> betList = new ArrayList<>();
        betList.add(bet1);
        Ticket ticket = new Ticket();
        ticket.setBets(betList);

        Exception exception
                = assertThrows(Exception.class, () -> betService.saveBetsForTicket(betList, ticket));
        assertEquals("Unable to save all Bets for Ticket!", exception.getMessage());
    }


    @Test
    public void testGetBetsForTicketApiResponse_Success() throws Exception {
        Integer ticketId = 1;
        List<Odd> odds = OddGenerator.generateUniqueOdds(2);
        OddGenerator.setOddGroupForOdds(odds, OddGroupGenerator.generateUniqueOddGroup());
        OddGenerator.setFixtureForOdds(odds);
        List<Bet> betList = BetGenerator.generateBetsForOdds(odds);
        when(betRepository.findAllByTicketId(ticketId)).thenReturn(betList);

        List<BetInfoDTO> betInfoDTOS = new ArrayList<>();
        for (Bet bet : betList) {
            Odd odd = bet.getOdd();
            Fixture fixture = odd.getFixture();
            OddGroup oddGroup = odd.getOddGroup();
            BetInfoDTO betInfoDTO = BetMapper.betToBetInfoDTO(bet, oddGroup, odd, fixture);
            betInfoDTOS.add(betInfoDTO);
        }
        // Act
        ApiResponse<List<BetInfoDTO>> response = (ApiResponse<List<BetInfoDTO>>) betService.getBetsForTicketApiResponse(ticketId);

        // Assert
        assertIterableEquals(betInfoDTOS, response.getData());
        assertTrue(response.getErrorMessages().isEmpty());
        // Add more assertions based on your specific requirements
    }

    @Test
    public void testGetBetsForTicketApiResponseDatabaseError() {
        Integer ticketId = 1;
        when(betRepository.findAllByTicketId(ticketId)).thenThrow(new RuntimeException("Simulated error"));

        ApiResponse<List<BetInfoDTO>> response = (ApiResponse<List<BetInfoDTO>>) betService.getBetsForTicketApiResponse(ticketId);
        assertNull(response.getData());
        assertFalse(response.getErrorMessages().isEmpty());
        assertEquals("Error getting bets for ticket id = " + ticketId + "!", response.getErrorMessages().get(0));
    }

    @Test
    public void testGetBetsForTicketApiResponseInvalidBets() {
        Integer ticketId = 1;
        List<Bet> betList = BetGenerator.generateUniqueBets(2);
        when(betRepository.findAllByTicketId(ticketId)).thenReturn(betList);

        ApiResponse<List<BetInfoDTO>> response = (ApiResponse<List<BetInfoDTO>>) betService.getBetsForTicketApiResponse(ticketId);
        assertNull(response.getData());
        assertFalse(response.getErrorMessages().isEmpty());
        assertEquals("Error getting bets for ticket id = " + ticketId + "!", response.getErrorMessages().get(0));
    }
}
