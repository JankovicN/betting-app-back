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

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final TicketRepository ticketRepository;
    private final PaymentService paymentService;
    private final UserService userService;
    private final BetService betService;


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
            Page<Ticket> ticketPage = ticketRepository.findByUserUsernameAndDateOrderByDateDesc(username, startDateTime, endDateTime, pageable);
            PageDTO<TicketBasicDTO> ticketDtoPage = createPageDtoForTickets(ticketPage, pageable);
            response.setData(ticketDtoPage);
        } catch (Exception e) {
            response.addErrorMessage("Error getting Tickets for date " + startDateTime + ", try again later!");
            logger.error("Error getting Tickets for username = " + username + "!", e);
        }
        return response;
    }

    private PageDTO<TicketBasicDTO> createPageDtoForTickets(Page<Ticket> tickets, Pageable pageable) throws Exception {
        // Creating a List of TicketBasicDTO,
        // 1. Map the ticket to the DTO
        // 2. Filter if there are any null values ( meaning mapping wasn't successful )
        // 3. Create a list of DTO objects
        List<TicketBasicDTO> basicTicketDtoList = tickets.map(ticket -> {
            try {
                return TicketMapper.ticketToTicketBasicDTO(ticket);
            } catch (Exception e) {
                logger.error("Error while mapping cancel ticket to DTO");
                return null;
            }
        }).filter(ticketDTO -> ticketDTO != null).stream().toList();

        // Create a Page instance of TicketBasicDTO List
        Page<TicketBasicDTO> basicTicketPage = new PageImpl<>(basicTicketDtoList, pageable, tickets.getTotalElements());

        // Map the Page instance to PageDTO adn return that value
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
            Page<Ticket> ticketPage = ticketRepository.findAllByDateOrderByDateDesc(startDateTime,endDateTime, pageable);
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
                response.addErrorMessage("Ticket is invalid!");
            } else if (ticketDTO.getUsername() == null || ticketDTO.getUsername().isBlank()) {
                logger.error("Username is missing! \n" + ticketDTO);
                response.addErrorMessage("Username is missing!");
            } else if (ticketDTO.getBets()==null || ticketDTO.getBets().isEmpty()){
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
                Ticket invaliTicket = new Ticket();
                invaliTicket.setState(Constants.INVALID_DATA);
                return invaliTicket;
            } else {
                canceledTicket.setState(Constants.TICKET_CANCELED);
                ticketRepository.save(canceledTicket);

                User user = canceledTicket.getUser();
                Integer userID = user.getId();
                paymentService.addPayment(userID, canceledTicket.getWager(), Constants.PAYMENT_REFUND);

                logger.info("Successfully canceled Ticket!");
                return canceledTicket;
            }
        } catch (EntityNotFoundException e) {
            logger.error("Error while trying to delete Ticket, try again later!", e);
            return null;
        } catch (Exception e) {
            logger.error("Error while trying to cancel Ticket, try again later!", e);
            return null;
        }
    }

    @Override
    public ApiResponse<?> cancelTicketApiResponse(Integer ticketID) {
        ApiResponse<?> response = new ApiResponse<>();
        try {
            Ticket canceledTicket = cancelTicket(ticketID);
            if (canceledTicket == null) {
                response.addErrorMessage("Error canceling Ticket, try again later!");
                logger.info("Error creating response for canceling Ticket!");
            } else if (LocalDateTime.of(1, 1, 1, 1, 1).isEqual(canceledTicket.getDate())) {
                response.addErrorMessage("Time for canceling ticket has passed, ticket can be canceled 5 minutes after being played!");
                logger.info("Time for canceling ticket has passed!");
            } else if (canceledTicket.getState().equals(Constants.INVALID_DATA)) {
                response.addErrorMessage("Cannot cancel Ticket!");
                logger.info("Invalid Ticket state, unable to cancel ticket!");
            } else {
                response.addInfoMessage("Successfully canceled Ticket!");
                logger.info("Successfully created response of cancelable Tickets!");
            }
        } catch (Exception e) {
            response.addErrorMessage("Error canceling Ticket, try again later!");
            logger.info("Error creating response for canceling Ticket!", e);
        }
        return response;
    }

    @Override
    public ApiResponse<?> handleGetUserTickets(String username, Optional<String> date, Pageable pageable) {
        ApiResponse<?> response;
        if (date.isPresent()) {
            String dateString = date.get();
            LocalDate dateValue = Utility.parseDate(dateString);
            LocalDateTime startDateTime = LocalDateTime.of(dateValue, LocalTime.of(0,0,0));
            LocalDateTime endDateTime = LocalDateTime.of(dateValue, LocalTime.of(23,59,59));
            response = getUserTickets(username, startDateTime,endDateTime, pageable);
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
            LocalDateTime startDateTime = LocalDateTime.of(dateValue, LocalTime.of(0,0,0));
            LocalDateTime endDateTime = LocalDateTime.of(dateValue, LocalTime.of(23,59,59));
            response = getAllTickets(startDateTime,endDateTime, pageable);
        } else {
            response = getAllTickets(pageable);
        }
        return response;
    }

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

    private void setNewTicketFields(Ticket ticket) {
        if (ticket == null) {
            logger.error("Error setting new ticket values for Ticket = " + ticket + "!");
        } else {
            ticket.setDate(LocalDateTime.now());
            ticket.setState(Constants.TICKET_UNPROCESSED);
        }
    }

}