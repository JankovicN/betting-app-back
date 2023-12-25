package rs.ac.bg.fon.BettingAppBack.service.interfaceTest;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import rs.ac.bg.fon.BettingAppBack.util.TicketDTOGenerator;
import rs.ac.bg.fon.BettingAppBack.util.TicketGenerator;
import rs.ac.bg.fon.BettingAppBack.util.UserGenerator;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Page.PageDTO;
import rs.ac.bg.fon.dtos.Ticket.TicketBasicDTO;
import rs.ac.bg.fon.dtos.Ticket.TicketCancelDTO;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Payment;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.mappers.BetMapper;
import rs.ac.bg.fon.mappers.TicketMapper;
import rs.ac.bg.fon.repository.TicketRepository;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.service.PaymentService;
import rs.ac.bg.fon.service.TicketService;
import rs.ac.bg.fon.service.UserService;
import rs.ac.bg.fon.utility.ApiResponse;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public abstract class TicketServiceTest {

    protected TicketService ticketService;

    protected TicketRepository ticketRepository;

    protected BetService betService;

    protected UserService userService;

    protected PaymentService paymentService;

    // Test update all Tickets
    @Test
    void testUpdateAllTicketsSuccess() throws Exception {

        doNothing().when(betService).updateAllBets();

        doNothing().when(ticketRepository).updateAllTickets();

        ApiResponse<?> apiResponse = ticketService.updateAllTickets();

        assertEquals(0, apiResponse.getErrorMessages().size());
        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully updated Tickets!", apiResponse.getInfoMessages().get(0));
    }

    @Test
    void testUpdateAllTicketsErrorIsThrown() throws Exception {

        doThrow(new RuntimeException("Simulated exception!")).when(betService).updateAllBets();


        ApiResponse<?> apiResponse = ticketService.updateAllTickets();

        assertEquals(0, apiResponse.getInfoMessages().size());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error updating Tickets, try again later!", apiResponse.getErrorMessages().get(0));
    }


    // Test get Tickets made by User
    @Test
    void testGetUserTicketsSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        when(ticketRepository.findByUserUsernameOrderByDateDesc("username", pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.getUserTickets("username", pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testGetUserTicketsInvalidTicket() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        ticketList.get(0).setOdd(0.5);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        when(ticketRepository.findByUserUsernameOrderByDateDesc("username", pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.getUserTickets("username", pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(1, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testGetUserTicketsDatabaseError() {
        Pageable pageable = Pageable.unpaged();

        when(ticketRepository.findByUserUsernameOrderByDateDesc("username", pageable)).thenThrow(new RuntimeException("Simulated exception"));

        ApiResponse<?> apiResponse = ticketService.getUserTickets("username", pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error getting Tickets, try again later!", apiResponse.getErrorMessages().get(0));
    }


    // Test get Tickets made by User for date range

    @Test
    void testGetUserTicketsForDateRangeSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 2, 2);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 10, 1, 2, 2);

        when(ticketRepository.findByUserUsernameAndDateOrderByDateDesc("username", startDateTime, endDateTime, pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.getUserTickets("username", startDateTime, endDateTime, pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testGetUserTicketsForDateRangeInvalidDateRange() {
        Pageable pageable = Pageable.unpaged();

        LocalDateTime startDateTime = LocalDateTime.of(2023, 10, 1, 2, 2);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 1, 2, 2);

        ApiResponse<?> apiResponse = ticketService.getUserTickets("username", startDateTime, endDateTime, pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Date range is invalid, start date must be before end date!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testGetUserTicketsForDateRangeInvalidTicket() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        ticketList.get(0).setOdd(0.5);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 2, 2);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 10, 1, 2, 2);

        when(ticketRepository.findByUserUsernameAndDateOrderByDateDesc("username", startDateTime, endDateTime, pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.getUserTickets("username", startDateTime, endDateTime, pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(1, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testGetUserTicketsForDateRangeDatabaseError() {
        Pageable pageable = Pageable.unpaged();

        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 2, 2);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 10, 1, 2, 2);

        ApiResponse<?> apiResponse = ticketService.getUserTickets("username", startDateTime, endDateTime, pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error getting Tickets for date " + startDateTime + ", try again later!", apiResponse.getErrorMessages().get(0));
    }


    // Test get all Tickets
    @Test
    void testGetAllTicketsSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        when(ticketRepository.findAllByOrderByDateDesc(pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.getAllTickets(pageable);

        assertEquals(0, apiResponse.getErrorMessages().size());
        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(page.getTotalPages(), ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getTotalPages());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testGetAllTicketsDatabaseError() {
        Pageable pageable = Pageable.unpaged();

        ApiResponse<?> apiResponse = ticketService.getAllTickets(pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error getting Tickets, try again later!", apiResponse.getErrorMessages().get(0));
    }


    // Test get all Tickets for date range
    @Test
    void testGetAllTicketsForDateRangeSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 2, 2);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 10, 1, 2, 2);

        when(ticketRepository.findAllByDateOrderByDateDesc(startDateTime, endDateTime, pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.getAllTickets(startDateTime, endDateTime, pageable);

        assertEquals(0, apiResponse.getErrorMessages().size());
        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(page.getTotalPages(), ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getTotalPages());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testGetAllTicketsForDateRangeInvalidDateRange() {
        Pageable pageable = Pageable.unpaged();

        LocalDateTime startDateTime = LocalDateTime.of(2023, 10, 1, 2, 2);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 1, 2, 2);

        ApiResponse<?> apiResponse = ticketService.getAllTickets(startDateTime, endDateTime, pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Date range is invalid, start date must be before end date!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testGetAllTicketsForDateRangeDatabaseError() {
        Pageable pageable = Pageable.unpaged();

        LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 1, 2, 2);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 10, 1, 2, 2);

        ApiResponse<?> apiResponse = ticketService.getAllTickets(startDateTime, endDateTime, pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error getting Tickets, try again later!", apiResponse.getErrorMessages().get(0));
    }


    // Test process Tickets
    @Test
    void testProcessTicketsSuccess() {

        doNothing().when(ticketRepository).processTickets(any(LocalDateTime.class));

        ticketService.processTickets();

        verify(ticketRepository, times(1)).processTickets(any(LocalDateTime.class));
    }

    // Test add new Ticket
    @Test
    void testAddNewTicketApiResponseSuccess() throws Exception {
        TicketDTO ticketDTO = TicketDTOGenerator.generateUniqueTicket();

        User user = UserGenerator.generateUniqueUser();
        user.setUsername(ticketDTO.getUsername());

        List<Bet> betList = BetMapper.betDTOListToBetList(ticketDTO.getBets());
        Ticket ticket = TicketMapper.ticketDTOToTicket(ticketDTO);
        ticket.setBets(betList);

        when(userService.getUser(ticketDTO.getUsername())).thenReturn(user);

        when(paymentService.canUserPay(user.getId(), ticketDTO.getWager().negate())).thenReturn(true);
        when(paymentService.addPayment(user.getId(), ticketDTO.getWager().negate(), Constants.PAYMENT_WAGER)).thenReturn(new Payment());

        ticket.setDate(LocalDateTime.now());
        ticket.setState(Constants.TICKET_UNPROCESSED);
        ticket.setUser(user);
        BigDecimal ticketOdds = BigDecimal.valueOf(ticket.getOdd());
        ticketOdds = ticketOdds.setScale(2, BigDecimal.ROUND_HALF_UP);
        ticket.setOdd(ticketOdds.doubleValue());

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        doNothing().when(betService).saveBetsForTicket(anyList(), any(Ticket.class));

        ApiResponse<?> apiResponse = ticketService.addNewTicketApiResponse(ticketDTO);

        assertEquals(0, apiResponse.getErrorMessages().size());
        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully played Ticket!", apiResponse.getInfoMessages().get(0));
    }

    @Test
    void testAddNewTicketApiResponseTicketIsNull() {
        ApiResponse<?> apiResponse = ticketService.addNewTicketApiResponse(null);

        assertEquals(0, apiResponse.getInfoMessages().size());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Ticket is missing!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testAddNewTicketApiResponseUsernameIsMissing() {
        TicketDTO ticketDTO = TicketDTOGenerator.generateUniqueTicket();
        ticketDTO.setUsername("");

        ApiResponse<?> apiResponse = ticketService.addNewTicketApiResponse(ticketDTO);

        assertEquals(0, apiResponse.getInfoMessages().size());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Username is missing!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testAddNewTicketApiResponseNoBets() {
        TicketDTO ticketDTO = TicketDTOGenerator.generateUniqueTicket();
        ticketDTO.setBets(new ArrayList<>());

        ApiResponse<?> apiResponse = ticketService.addNewTicketApiResponse(ticketDTO);

        assertEquals(0, apiResponse.getInfoMessages().size());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Ticket must contain at least one bet!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testAddNewTicketApiResponseError() {
        TicketDTO ticketDTO = TicketDTOGenerator.generateUniqueTicket();

        when(userService.getUser(ticketDTO.getUsername())).thenThrow(new RuntimeException("Simulated exception!"));

        ApiResponse<?> apiResponse = ticketService.addNewTicketApiResponse(ticketDTO);

        assertEquals(0, apiResponse.getInfoMessages().size());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Unable to create new Ticket, try again later!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testAddNewTicketApiResponseInsufficientFunds() {
        TicketDTO ticketDTO = TicketDTOGenerator.generateUniqueTicket();

        User user = UserGenerator.generateUniqueUser();
        user.setUsername(ticketDTO.getUsername());

        when(userService.getUser(ticketDTO.getUsername())).thenReturn(user);

        when(paymentService.canUserPay(user.getId(), ticketDTO.getWager().negate())).thenReturn(false);

        ApiResponse<?> apiResponse = ticketService.addNewTicketApiResponse(ticketDTO);

        assertEquals(0, apiResponse.getInfoMessages().size());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Insufficient funds!", apiResponse.getErrorMessages().get(0));
    }


    // Test payout all Users
    @Test
    void testPayoutUsersSuccess() {
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        for (Ticket ticket : ticketList) {
            ticket.setUser(UserGenerator.generateUniqueUser());
        }

        when(ticketRepository.findByState(Constants.TICKET_WIN)).thenReturn(ticketList);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());

        when(paymentService.addPayment(any(Payment.class))).thenReturn(new Payment());

        ticketService.payoutUsers();

        verify(ticketRepository, times(2)).save(any(Ticket.class));
        verify(paymentService, times(2)).addPayment(any(Payment.class));
    }

    @Test
    void testPayoutUsersNoTicketsToUpdate() {

        when(ticketRepository.findByState(Constants.TICKET_WIN)).thenReturn(new ArrayList<>());

        ticketService.payoutUsers();

        verify(ticketRepository, times(0)).save(any(Ticket.class));
        verify(paymentService, times(0)).addPayment(any(Payment.class));
    }


    // Test handle get cancel Tickets API Response
    @Test
    void testHandleCancelTicketsApiUsernameIsPresentSuccess() {
        User user = UserGenerator.generateUniqueUser();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        for (Ticket ticket : ticketList) {
            ticket.setUser(user);
        }
        Pageable pageable = Pageable.unpaged();
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        when(ticketRepository.getTicketsAfterDateTime(any(LocalDateTime.class), anyString(), anyString(), any(Pageable.class))).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.handleCancelTicketsAPI(Optional.of(user.getUsername()), pageable);


        assertEquals(0, apiResponse.getErrorMessages().size());
        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketCancelDTO>) apiResponse.getData()).getContent().size());
        assertEquals(page.getTotalPages(), ((PageDTO<TicketCancelDTO>) apiResponse.getData()).getTotalPages());
        assertEquals(TicketCancelDTO.class, ((PageDTO<TicketCancelDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testHandleCancelTicketsApiUsernameIsPresentDatabaseError() {
        User user = UserGenerator.generateUniqueUser();
        Pageable pageable = Pageable.unpaged();

        when(ticketRepository.getTicketsAfterDateTime(any(LocalDateTime.class), anyString(), anyString(), any(Pageable.class)))
                .thenThrow(new RuntimeException("Simulated Error"));

        ApiResponse<?> apiResponse = ticketService.handleCancelTicketsAPI(Optional.of(user.getUsername()), pageable);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error getting Tickets to cancel, try again later!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testHandleCancelTicketsApiSuccess() {
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        for (Ticket ticket : ticketList) {
            ticket.setUser(UserGenerator.generateUniqueUser());
        }
        Pageable pageable = Pageable.unpaged();
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        when(ticketRepository.getTicketsAfterDateTime(any(LocalDateTime.class), anyString(), any(Pageable.class))).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.handleCancelTicketsAPI(Optional.empty(), pageable);


        assertEquals(0, apiResponse.getErrorMessages().size());
        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketCancelDTO>) apiResponse.getData()).getContent().size());
        assertEquals(page.getTotalPages(), ((PageDTO<TicketCancelDTO>) apiResponse.getData()).getTotalPages());
        assertEquals(TicketCancelDTO.class, ((PageDTO<TicketCancelDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testHandleCancelTicketsApiDatabaseError() {
        Pageable pageable = Pageable.unpaged();

        when(ticketRepository.getTicketsAfterDateTime(any(LocalDateTime.class), anyString(), any(Pageable.class)))
                .thenThrow(new RuntimeException("Simulated Error"));


        ApiResponse<?> apiResponse = ticketService.handleCancelTicketsAPI(Optional.empty(), pageable);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error getting Tickets to cancel, try again later!", apiResponse.getErrorMessages().get(0));
    }

    // Test cancel Ticket
    @Test
    void testCancelTicketSuccess() {
        Ticket ticketToCancel = TicketGenerator.generateUniqueTicket();
        ticketToCancel.setUser(UserGenerator.generateUniqueUser());

        Ticket canceledTicket = TicketGenerator.generateUniqueTicket();
        canceledTicket.setState(Constants.TICKET_CANCELED);

        when(ticketRepository.findById(ticketToCancel.getId())).thenReturn(Optional.of(ticketToCancel));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(canceledTicket);

        when(paymentService.addPayment(anyInt(), any(BigDecimal.class), anyString())).thenReturn(new Payment());

        Ticket ticket = ticketService.cancelTicket(ticketToCancel.getId());

        assertEquals(Constants.TICKET_CANCELED, ticket.getState());
    }

    @Test
    void testCancelTicketTimeForCancellationHasPassed() {
        Ticket ticketToCancel = TicketGenerator.generateUniqueTicket();
        ticketToCancel.setUser(UserGenerator.generateUniqueUser());
        ticketToCancel.setDate(LocalDateTime.of(2023, 1, 1, 1, 1));

        when(ticketRepository.findById(ticketToCancel.getId())).thenReturn(Optional.of(ticketToCancel));

        Ticket ticket = ticketService.cancelTicket(ticketToCancel.getId());

        assertEquals(LocalDateTime.of(1, 1, 1, 1, 1), ticket.getDate());
        assertEquals(ticketToCancel.getUser(), ticket.getUser());
        assertEquals(ticketToCancel.getBets(), ticket.getBets());
    }

    @Test
    void testCancelTicketInvalidState() {
        Ticket ticketToCancel = TicketGenerator.generateUniqueTicket();
        ticketToCancel.setUser(UserGenerator.generateUniqueUser());
        ticketToCancel.setDate(LocalDateTime.now());
        ticketToCancel.setState(Constants.TICKET_PROCESSED);

        when(ticketRepository.findById(ticketToCancel.getId())).thenReturn(Optional.of(ticketToCancel));

        Ticket ticket = ticketService.cancelTicket(ticketToCancel.getId());

        assertEquals(Constants.INVALID_DATA, ticket.getState());
        assertNull(ticket.getUser());
        assertEquals(new ArrayList<>(), ticket.getBets());
    }

    @Test
    void testCancelTicketNotFound() {

        when(ticketRepository.findById(anyInt())).thenThrow(new EntityNotFoundException("Entity not found"));

        Ticket ticket = ticketService.cancelTicket(-1);

        assertNull(ticket);
    }

    // Test cancel Ticket API Response
    @Test
    void testCancelTicketApiResponseSuccess() {
        Ticket ticketToCancel = TicketGenerator.generateUniqueTicket();
        ticketToCancel.setUser(UserGenerator.generateUniqueUser());

        Ticket canceledTicket = TicketGenerator.generateUniqueTicket();
        canceledTicket.setState(Constants.TICKET_CANCELED);

        when(ticketRepository.findById(ticketToCancel.getId())).thenReturn(Optional.of(ticketToCancel));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(canceledTicket);

        when(paymentService.addPayment(anyInt(), any(BigDecimal.class), anyString())).thenReturn(new Payment());

        ApiResponse<?> apiResponse = ticketService.cancelTicketApiResponse(ticketToCancel.getId());

        assertEquals(1, apiResponse.getInfoMessages().size());
        assertEquals("Successfully canceled Ticket!", apiResponse.getInfoMessages().get(0));
    }

    @Test
    void testCancelTicketApiResponseTimeForCancellationHasPassed() {
        Ticket ticketToCancel = TicketGenerator.generateUniqueTicket();
        ticketToCancel.setUser(UserGenerator.generateUniqueUser());
        ticketToCancel.setDate(LocalDateTime.of(2023, 1, 1, 1, 1));

        when(ticketRepository.findById(ticketToCancel.getId())).thenReturn(Optional.of(ticketToCancel));

        ApiResponse<?> apiResponse = ticketService.cancelTicketApiResponse(ticketToCancel.getId());

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Time for canceling ticket has passed, ticket can be canceled 5 minutes after being played!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testCancelTicketApiResponseInvalidState() {
        Ticket ticketToCancel = TicketGenerator.generateUniqueTicket();
        ticketToCancel.setUser(UserGenerator.generateUniqueUser());
        ticketToCancel.setState(Constants.TICKET_PROCESSED);

        when(ticketRepository.findById(ticketToCancel.getId())).thenReturn(Optional.of(ticketToCancel));

        ApiResponse<?> apiResponse = ticketService.cancelTicketApiResponse(ticketToCancel.getId());

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Cannot cancel Ticket!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testCancelTicketApiResponseNotFound() {

        when(ticketRepository.findById(anyInt())).thenThrow(new EntityNotFoundException("Entity not found"));

        Ticket ticket = ticketService.cancelTicket(-1);

        ApiResponse<?> apiResponse = ticketService.cancelTicketApiResponse(-1);

        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error canceling Ticket, try again later!", apiResponse.getErrorMessages().get(0));
    }

    // Test handle get Tickets made by User API Response
    @Test
    void testHandleUserTicketsDateIsPresentSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        String date = "2023-01-01";

        when(ticketRepository.findByUserUsernameAndDateOrderByDateDesc(anyString(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.handleGetUserTickets("username", Optional.of(date), pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testHandleUserTicketsDateIsPresentInvalidTicket() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        ticketList.get(0).setOdd(0.5);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        String date = "2023-01-01";

        when(ticketRepository.findByUserUsernameAndDateOrderByDateDesc(anyString(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.handleGetUserTickets("username", Optional.of(date), pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(1, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testHandleUserTicketsDateIsPresentDatabaseError() {
        Pageable pageable = Pageable.unpaged();

        String date = "2023-01-01";

        ApiResponse<?> apiResponse = ticketService.handleGetUserTickets("username", Optional.of(date), pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertTrue(apiResponse.getErrorMessages().get(0).contains("Error getting Tickets for date "));
    }

    @Test
    void testHandleUserTicketsSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        when(ticketRepository.findByUserUsernameOrderByDateDesc("username", pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.handleGetUserTickets("username", Optional.empty(), pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testHandleUserTicketsInvalidTicket() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        ticketList.get(0).setOdd(0.5);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        when(ticketRepository.findByUserUsernameOrderByDateDesc("username", pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.handleGetUserTickets("username", Optional.empty(), pageable);

        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(1, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testHandleUserTicketsDatabaseError() {
        Pageable pageable = Pageable.unpaged();

        when(ticketRepository.findByUserUsernameOrderByDateDesc("username", pageable)).thenThrow(new RuntimeException("Simulated exception"));

        ApiResponse<?> apiResponse = ticketService.handleGetUserTickets("username", Optional.empty(), pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error getting Tickets, try again later!", apiResponse.getErrorMessages().get(0));
    }
    // Test get all Tickets API Response

    @Test
    void testHandleGetAllTicketsSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        when(ticketRepository.findAllByOrderByDateDesc(pageable)).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.handleGetAllTickets(Optional.empty(), pageable);

        assertEquals(0, apiResponse.getErrorMessages().size());
        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(page.getTotalPages(), ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getTotalPages());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testHandleGetAllTicketsDatabaseError() {
        Pageable pageable = Pageable.unpaged();

        ApiResponse<?> apiResponse = ticketService.handleGetAllTickets(Optional.empty(), pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error getting Tickets, try again later!", apiResponse.getErrorMessages().get(0));
    }

    @Test
    void testHandleGetAllTicketsDateIsPresentSuccess() {
        Pageable pageable = Pageable.unpaged();
        List<Ticket> ticketList = TicketGenerator.generateUniqueTickets(2);
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, ticketList.size());

        String date = "2023-01-01";

        when(ticketRepository.findAllByDateOrderByDateDesc(any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);

        ApiResponse<?> apiResponse = ticketService.handleGetAllTickets(Optional.of(date), pageable);

        assertEquals(0, apiResponse.getErrorMessages().size());
        assertEquals(PageDTO.class, apiResponse.getData().getClass());
        assertEquals(2, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().size());
        assertEquals(page.getTotalPages(), ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getTotalPages());
        assertEquals(TicketBasicDTO.class, ((PageDTO<TicketBasicDTO>) apiResponse.getData()).getContent().get(0).getClass());
    }

    @Test
    void testHandleGetAllTicketsDateIsPresentDatabaseError() {
        Pageable pageable = Pageable.unpaged();

        String date = "2023-01-01";

        ApiResponse<?> apiResponse = ticketService.handleGetAllTickets(Optional.of(date), pageable);

        assertNull(apiResponse.getData());
        assertEquals(1, apiResponse.getErrorMessages().size());
        assertEquals("Error getting Tickets, try again later!", apiResponse.getErrorMessages().get(0));
    }


}
