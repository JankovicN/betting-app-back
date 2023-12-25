package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import rs.ac.bg.fon.mappers.PageMapper;
import rs.ac.bg.fon.mappers.TicketMapper;
import rs.ac.bg.fon.repository.TicketRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.Utility;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a service layer class responsible for implementing all Ticket related methods.
 * Available API method implementations: GET, POST, PATCH, DELETE
 *
 * @author Janko
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    /**
     * Instance of Logger class, responsible for displaying messages that contain information about the success of methods inside Ticket service class.
     */
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    /**
     * Instance of Ticket repository class, responsible for interacting with ticket table in database.
     */
    private final TicketRepository ticketRepository;

    /**
     * Instance of Payment service class, responsible for executing any logic related to Payment entity.
     */
    private final PaymentService paymentService;

    /**
     * Instance of User service class, responsible for executing any logic related to User entity.
     */
    private final UserService userService;

    /**
     * Instance of Bet service class, responsible for executing any logic related to Bet entity.
     */
    private final BetService betService;

    /**
     * Adds new ticket to database. Returns instance of saved ticket from database.
     *
     * @param ticket instance of Ticket class that is being saved.
     * @return instance of Ticket class that is saved in database,
     * or null if ticket has invalid fields or if error occurs.
     */
    private Ticket save(Ticket ticket) {
        try {
            if (ticket == null
                    || ticket.getUser() == null
                    || ticket.getDate() == null
                    || ticket.getState() == null
                    || ticket.getWager() == null
                    || ticket.getWager().compareTo(BigDecimal.valueOf(20)) < 0
                    || ticket.getBets() == null
                    || ticket.getBets().isEmpty()
                    || ticket.getTotalWin() == null
                    || ticket.getTotalWin().compareTo(BigDecimal.valueOf(20)) < 0
                    || ticket.getOdd() <= 0.0) {
                return null;
            }
            Ticket savedTicket = ticketRepository.save(ticket);
            logger.info("Successfully saved Ticket " + savedTicket + "!");
            return savedTicket;
        } catch (Exception e) {
            logger.error("Error while trying to save Ticket " + ticket + "!\n" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ApiResponse<?> updateAllTickets() {
        ApiResponse<List<Ticket>> response = new ApiResponse<>();
        try {
            betService.updateAllBets();
            ticketRepository.updateAllTickets();
            response.addInfoMessage("Successfully updated Tickets!");
            logger.info("Successfully updated Tickets!");
        } catch (Exception e) {
            response.addErrorMessage("Error updating Tickets, try again later!");
            logger.error("Error updating Tickets, try again later!", e);
        }
        return response;
    }

    @Override
    public ApiResponse<?> getUserTickets(String username, Pageable pageable) {
        ApiResponse<PageDTO<TicketBasicDTO>> response = new ApiResponse<>();
        try {
            Page<Ticket> ticketPage = ticketRepository.findByUserUsernameOrderByDateDesc(username, pageable);
            PageDTO<TicketBasicDTO> ticketDtoPage = createPageDtoForTickets(ticketPage, pageable);
            response.setData(ticketDtoPage);
        } catch (Exception e) {
            response.addErrorMessage("Error getting Tickets, try again later!");
            logger.error("Error getting Tickets for username = " + username + "!", e);
        }
        return response;
    }

    @Override
    public ApiResponse<?> getUserTickets(String username, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable) {
        ApiResponse<PageDTO<TicketBasicDTO>> response = new ApiResponse<>();
        try {
            if (startDateTime.isAfter(endDateTime)) {
                logger.error("Error getting Tickets for date range " + startDateTime + " - " + endDateTime + "!");
                response.addErrorMessage("Date range is invalid, start date must be before end date!");
            } else {
                Page<Ticket> ticketPage = ticketRepository.findByUserUsernameAndDateOrderByDateDesc(username, startDateTime, endDateTime, pageable);
                PageDTO<TicketBasicDTO> ticketDtoPage = createPageDtoForTickets(ticketPage, pageable);
                response.setData(ticketDtoPage);
            }
        } catch (Exception e) {
            response.addErrorMessage("Error getting Tickets for date " + startDateTime + ", try again later!");
            logger.error("Error getting Tickets for username = " + username + "!", e);
        }
        return response;
    }

    /**
     * Transforms list of tickets to DTO and adds them to PageDTO.
     * Returns PageDTO containing list of TicketBasicDTO objects
     *
     * @param tickets  list of tickets that are to be transformed to DTO.
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of PageDTO class, containing TicketBasicDTO objects.
     */
    private PageDTO<TicketBasicDTO> createPageDtoForTickets(Page<Ticket> tickets, Pageable pageable) throws Exception {
        List<TicketBasicDTO> basicTicketDtoList = tickets.map(ticket -> {
            try {
                return TicketMapper.ticketToTicketBasicDTO(ticket);
            } catch (Exception e) {
                logger.error("Error while mapping cancel ticket to DTO");
                return null;
            }
        }).filter(ticketDTO -> ticketDTO != null).stream().toList();
        Page<TicketBasicDTO> basicTicketPage = new PageImpl<>(basicTicketDtoList, pageable, tickets.getTotalElements());
        return PageMapper.pageToPageDTO(basicTicketPage);
    }

    @Override
    public ApiResponse<?> getAllTickets(Pageable pageable) {
        ApiResponse<PageDTO<TicketBasicDTO>> response = new ApiResponse<>();
        try {
            Page<Ticket> ticketPage = ticketRepository.findAllByOrderByDateDesc(pageable);
            PageDTO<TicketBasicDTO> ticketDtoPage = createPageDtoForTickets(ticketPage, pageable);
            ticketDtoPage.setTotalPages(ticketPage.getTotalPages());
            response.setData(ticketDtoPage);
        } catch (Exception e) {
            response.addErrorMessage("Error getting Tickets, try again later!");
            logger.error("Error getting all Tickets!", e);
        }
        return response;
    }

    @Override
    public ApiResponse<?> getAllTickets(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable) {
        ApiResponse<PageDTO<TicketBasicDTO>> response = new ApiResponse<>();
        try {

            if (startDateTime.isAfter(endDateTime)) {
                logger.error("Error getting Tickets for date range " + startDateTime + " - " + endDateTime + "!");
                response.addErrorMessage("Date range is invalid, start date must be before end date!");
            } else {
                Page<Ticket> ticketPage = ticketRepository.findAllByDateOrderByDateDesc(startDateTime, endDateTime, pageable);
                PageDTO<TicketBasicDTO> ticketDtoPage = createPageDtoForTickets(ticketPage, pageable);
                ticketDtoPage.setTotalPages(ticketPage.getTotalPages());
                response.setData(ticketDtoPage);
            }
        } catch (Exception e) {
            response.addErrorMessage("Error getting Tickets, try again later!");
            logger.error("Error getting all Tickets!", e);
        }
        return response;
    }

    @Override
    public void processTickets() {
        try {
            LocalDateTime oldDate = LocalDateTime.now().minusMinutes(5);
            ticketRepository.processTickets(oldDate);
            logger.info("Successfully processed Tickets!");
        } catch (Exception e) {
            logger.error("Error while processing Tickets, try again later!", e);
        }
    }

    @Transactional
    @Override
    public ApiResponse<?> addNewTicketApiResponse(TicketDTO ticketDTO) {

        ApiResponse<?> response = new ApiResponse<>();
        try {
            if (ticketDTO == null) {
                logger.error("Ticket is null!");
                response.addErrorMessage("Ticket is missing!");
            } else if (ticketDTO.getUsername() == null || ticketDTO.getUsername().isBlank()) {
                logger.error("Username is missing! \n" + ticketDTO);
                response.addErrorMessage("Username is missing!");
            } else if (ticketDTO.getBets() == null || ticketDTO.getBets().isEmpty()) {
                logger.error("Ticket has no bets! \n" + ticketDTO);
                response.addErrorMessage("Ticket must contain at least one bet!");
            } else {
                User user = userService.getUser(ticketDTO.getUsername());
                List<Bet> betList = BetMapper.betDTOListToBetList(ticketDTO.getBets());
                Ticket ticket = TicketMapper.ticketDTOToTicket(ticketDTO);
                ticket.setBets(betList);

                if (paymentService.canUserPay(user.getId(), ticketDTO.getWager().negate())) {
                    setNewTicketFields(ticket);
                    ticket.setUser(user);

                    BigDecimal ticketOdds = BigDecimal.valueOf(ticket.getOdd());
                    ticketOdds = ticketOdds.setScale(2, BigDecimal.ROUND_HALF_UP);
                    ticket.setOdd(ticketOdds.doubleValue());

                    paymentService.addPayment(user.getId(), ticket.getWager().negate(), Constants.PAYMENT_WAGER);
                    ticket = save(ticket);
                    logger.info("Saved ticket " + ticket);
                    betService.saveBetsForTicket(betList, ticket);
                    response.addInfoMessage("Successfully played Ticket!");
                } else {
                    response.addErrorMessage("Insufficient funds!");
                    logger.error("Insufficient funds to create new Ticket!");
                }
            }
        } catch (Exception e) {
            response.addErrorMessage("Unable to create new Ticket, try again later!");
            logger.error("Error while creating new Ticket!", e);
        }
        return response;
    }

    @Override
    public void payoutUsers() {
        List<Ticket> listOfTickets = ticketRepository.findByState(Constants.TICKET_WIN);
        for (Ticket ticket : listOfTickets) {
            try {
                payoutUser(ticket);
            } catch (Exception e) {
                logger.error("Error while paying out Users!", e);
            }
        }
    }

    /**
     * Updates the ticket state to PAYOUT and processes the payment for winning tickets.
     *
     * @param ticket instance of Ticket class that is being updated.
     */
    @Transactional
    private void payoutUser(Ticket ticket) {
        try {
            ticket.setState(Constants.TICKET_PAYOUT);
            ticketRepository.save(ticket);
            Payment payment = new Payment();
            payment.setUser(ticket.getUser());
            payment.setPaymentType(Constants.PAYMENT_PAYOUT);
            payment.setAmount(ticket.getTotalWin());
            paymentService.addPayment(payment);
        } catch (Exception e) {
            logger.error("Error while paying out Ticket = " + ticket + "!", e);
        }
    }

    /**
     * Fetches all tickets that have are cancelable (their state is UNPROCESSED) for given page.
     * Returns Page containing list of tickets.
     *
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance Page class that has an array of tickets in it,
     * or empty page if there are no tickets to be canceled for given page.
     */
    private Page<Ticket> getCancelableTickets(Pageable pageable) {
        try {
            LocalDateTime cancelDateTime = LocalDateTime.now().minusMinutes(5);
            Page<Ticket> ticketList = ticketRepository.getTicketsAfterDateTime(cancelDateTime, Constants.TICKET_UNPROCESSED, pageable);
            logger.info("Successfully got cancelable Tickets!");
            return ticketList;
        } catch (Exception e) {
            logger.error("Error while trying to get cancelable Tickets, try again later!", e);
            return new PageImpl<>(Collections.emptyList(), Pageable.unpaged(), 0);
        }
    }

    /**
     * Fetches all tickets that have are cancelable (their state is UNPROCESSED) and played by user for given page.
     * Returns Page containing list of tickets.
     *
     * @param username String value representing username of user for which we are fetching tickets.
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance Page class that has an array of tickets in it,
     * or empty page if there are no tickets to be canceled for given page.
     */
    private Page<Ticket> getCancelableTickets(String username, Pageable pageable) {
        try {
            LocalDateTime cancelDateTime = LocalDateTime.now().minusMinutes(5);
            Page<Ticket> ticketList = ticketRepository.getTicketsAfterDateTime(cancelDateTime, username, Constants.TICKET_UNPROCESSED, pageable);
            logger.info("Successfully got cancelable Tickets!");
            return ticketList;
        } catch (Exception e) {
            logger.error("Error while trying to get cancelable Tickets, try again later!", e);
            return new PageImpl<>(Collections.emptyList(), Pageable.unpaged(), 0);
        }
    }

    /**
     * Fetches all tickets that have are cancelable (their state is UNPROCESSED) for given page.
     * Transforms page and tickets to DTO.
     * Returns PageDTO containing list of TicketCancelDTO objects.
     *
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing PageDTO that has an array of TicketCancelDTO in it,
     * or error message if operation fails.
     */
    private ApiResponse<?> getCancelableTicketsApiResponse(Pageable pageable) {
        ApiResponse<PageDTO<TicketCancelDTO>> response = new ApiResponse<>();
        try {
            // Getting page of Tickets that can be canceled
            Page<Ticket> tickets = getCancelableTickets(pageable);

            PageDTO<TicketCancelDTO> cancelTicketPageDTO = createPageDtoForCancelTickets(tickets, pageable);

            response.setData(cancelTicketPageDTO);
            logger.info("Successfully created response of cancelable Tickets!");
        } catch (Exception e) {
            response.addErrorMessage("Error getting Tickets to cancel, try again later!");
            logger.info("Error creating response of cancelable Tickets!");
        }
        return response;
    }

    /**
     * Fetches all tickets that have are cancelable (their state is UNPROCESSED) and played by user for given page.
     * Transforms page and tickets to DTO.
     * Returns PageDTO containing list of TicketCancelDTO objects.
     *
     * @param username String value representing username of user for which we are fetching tickets.
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing PageDTO that has an array of TicketCancelDTO in it,
     * or error message if operation fails.
     */
    private ApiResponse<?> getCancelableTicketsApiResponse(String username, Pageable pageable) {
        ApiResponse<PageDTO<TicketCancelDTO>> response = new ApiResponse<>();
        try {
            Page<Ticket> tickets = getCancelableTickets(username, pageable);

            PageDTO<TicketCancelDTO> cancelTicketPageDTO = createPageDtoForCancelTickets(tickets, pageable);

            response.setData(cancelTicketPageDTO);
            logger.info("Successfully created response of cancelable Tickets!");
        } catch (Exception e) {
            response.addErrorMessage("Error getting Tickets to cancel, try again later!");
            logger.info("Error creating response of cancelable Tickets!");
        }
        return response;
    }

    /**
     * Transforms Page object containing tickets to DTO.
     * Return PageDTO object containing TicketCancelDTO objects.
     *
     * @param tickets  instance of Page object containing tickets.
     * @param pageable instance of Pageable class, containing information about current page.
     * @return instance of PageDTO containing TicketCancelDTO objects.
     */
    private PageDTO<TicketCancelDTO> createPageDtoForCancelTickets(Page<Ticket> tickets, Pageable pageable) throws Exception {
        // Creating a List of TicketCancelDTOs,
        // 1. Map the ticket to the DTO
        // 2. Filter if there are any null values ( meaning mapping wasn't successful )
        // 3. Create a list of DTO objects
        List<TicketCancelDTO> cancelTicketDtoList = tickets.map(ticket -> {
            try {
                return TicketMapper.ticketToTicketCancelDTO(ticket, ticket.getUser());
            } catch (Exception e) {
                logger.error("Error while mapping cancel ticket to DTO");
                return null;
            }
        }).filter(ticketDTO -> ticketDTO != null).stream().toList();

        // Create a Page instance of CancelTicketDTOs List
        Page<TicketCancelDTO> cancelTicketPage = new PageImpl<>(cancelTicketDtoList, pageable, cancelTicketDtoList.size());

        // Map the Page instance to PageDTO adn return that value
        return PageMapper.pageToPageDTO(cancelTicketPage);
    }

    @Override
    public ApiResponse<?> handleCancelTicketsAPI(Optional<String> username, Pageable pageable) {
        ApiResponse<?> response;
        if (username.isPresent()) {
            String usernameValue = username.get();
            response = getCancelableTicketsApiResponse(usernameValue, pageable);
        } else {
            response = getCancelableTicketsApiResponse(pageable);
        }
        return response;
    }

    @Override
    public Ticket cancelTicket(Integer ticketID) {
        try {
            Ticket canceledTicket = ticketRepository.findById(ticketID)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

            LocalDateTime cancelDateTime = LocalDateTime.now().minusMinutes(5);
            if (canceledTicket.getDate().isBefore(cancelDateTime)) {
                logger.error("Time for canceling ticket has passed!");
                canceledTicket.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
                return canceledTicket;
            } else if (!canceledTicket.getState().equals(Constants.TICKET_UNPROCESSED)) {
                logger.error("Ticket state is invalid, it should be \"" + Constants.TICKET_UNPROCESSED + "\", but it is \"" + canceledTicket.getState() + "\"!");
                Ticket invalidTicket = new Ticket();
                invalidTicket.setState(Constants.INVALID_DATA);
                return invalidTicket;
            } else {
                canceledTicket.setState(Constants.TICKET_CANCELED);
                ticketRepository.save(canceledTicket);

                User user = canceledTicket.getUser();
                Integer userID = user.getId();
                paymentService.addPayment(userID, canceledTicket.getWager(), Constants.PAYMENT_REFUND);

                logger.info("Successfully canceled Ticket!");
                return canceledTicket;
            }
        } catch (Exception e) {
            logger.error("Error while trying to cancel Ticket, try again later!", e);
            return null;
        }
    }

    @Override
    public ApiResponse<?> cancelTicketApiResponse(Integer ticketID) {
        ApiResponse<?> response = new ApiResponse<>();
        Ticket canceledTicket = cancelTicket(ticketID);
        if (canceledTicket == null) {
            response.addErrorMessage("Error canceling Ticket, try again later!");
            logger.info("Error creating response for canceling Ticket!");
        } else if (canceledTicket.getState().equals(Constants.INVALID_DATA)) {
            response.addErrorMessage("Cannot cancel Ticket!");
            logger.info("Invalid Ticket state, unable to cancel ticket!");
        } else if (LocalDateTime.of(1, 1, 1, 1, 1).isEqual(canceledTicket.getDate())) {
            response.addErrorMessage("Time for canceling ticket has passed, ticket can be canceled 5 minutes after being played!");
            logger.info("Time for canceling ticket has passed!");
        } else {
            response.addInfoMessage("Successfully canceled Ticket!");
            logger.info("Successfully created response of cancelable Tickets!");
        }
        return response;
    }

    @Override
    public ApiResponse<?> handleGetUserTickets(String username, Optional<String> date, Pageable pageable) {
        ApiResponse<?> response;
        if (date.isPresent()) {
            String dateString = date.get();
            LocalDate dateValue = Utility.parseDate(dateString);
            LocalDateTime startDateTime = LocalDateTime.of(dateValue, LocalTime.of(0, 0, 0));
            LocalDateTime endDateTime = LocalDateTime.of(dateValue, LocalTime.of(23, 59, 59));
            response = getUserTickets(username, startDateTime, endDateTime, pageable);
        } else {
            response = getUserTickets(username, pageable);
        }
        return response;
    }

    @Override
    public ApiResponse<?> handleGetAllTickets(Optional<String> date, Pageable pageable) {
        ApiResponse<?> response;
        if (date.isPresent()) {
            String dateString = date.get();
            LocalDate dateValue = Utility.parseDate(dateString);
            LocalDateTime startDateTime = LocalDateTime.of(dateValue, LocalTime.of(0, 0, 0));
            LocalDateTime endDateTime = LocalDateTime.of(dateValue, LocalTime.of(23, 59, 59));
            response = getAllTickets(startDateTime, endDateTime, pageable);
        } else {
            response = getAllTickets(pageable);
        }
        return response;
    }

    /**
     * Sets the state and date of new ticket.
     *
     * @param ticket instance of Ticket class that is being created.
     */
    private void setNewTicketFields(Ticket ticket) {
        if (ticket == null) {
            logger.error("Error setting new ticket values for Ticket = " + ticket + "!");
        } else {
            ticket.setDate(LocalDateTime.now());
            ticket.setState(Constants.TICKET_UNPROCESSED);
        }
    }

}