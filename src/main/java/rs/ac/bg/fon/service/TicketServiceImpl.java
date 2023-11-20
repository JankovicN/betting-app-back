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

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public ApiResponse<?> getUserTickets(String username) {
        ApiResponse<List<TicketBasicDTO>> response = new ApiResponse<>();
        try {
            List<TicketBasicDTO> ticketBasicDTOS = new ArrayList<>();
            for (Ticket t : ticketRepository.findByUserUsernameOrderByDateDesc(username)) {
                TicketBasicDTO ticketBasicDTO = TicketMapper.ticketToTicketBasicDTO(t);
                ticketBasicDTOS.add(ticketBasicDTO);
            }
            response.setData(ticketBasicDTOS);
            logger.info("Successfully got Tickets for user = " + username + "!");
        } catch (Exception e) {
            response.addErrorMessage("Error getting Tickets, try again later!");
            logger.error("Error getting Tickets for username = " + username + "!", e);
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
            if (ticketDTO.getUsername() == null || ticketDTO.getUsername().isBlank()) {
                logger.error("Username is missing! \n" + ticketDTO);
                response.addErrorMessage("Username is missing!");
            } else {
                User user = userService.getUser(ticketDTO.getUsername());
                List<Bet> betList = BetMapper.betDTOListToBetList(ticketDTO.getBets());
                Ticket ticket = TicketMapper.ticketDTOToTicket(ticketDTO);

                if (paymentService.canUserPay(user.getId(), ticketDTO.getWager())) {
                    setNewTicketFields(ticket);
                    ticket.setUser(user);

                    paymentService.addPayment(user.getId(), ticket.getWager().negate(), Constants.PAYMENT_WAGER);
                    this.save(ticket);
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
            Page<Ticket> ticketList = ticketRepository.getTicketsAfterDateTime(cancelDateTime, pageable);
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
            Page<Ticket> ticketList = ticketRepository.getTicketsAfterDateTime(cancelDateTime, username, pageable);
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
                canceledTicket.setDate(null);
                return canceledTicket;
            } else if (canceledTicket.getState() != Constants.TICKET_UNPROCESSED) {
                logger.error("Ticket state is invalid, it should be \"" + Constants.TICKET_UNPROCESSED + "\", but it is \"" + canceledTicket.getState() + "\"!");
                canceledTicket.setState(Constants.INVALID_DATA);
                return canceledTicket;
            } else {
                canceledTicket.setState(Constants.TICKET_CANCELED);
                ticketRepository.save(canceledTicket);
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
        ApiResponse<Ticket> response = new ApiResponse<>();
        try {
            Ticket canceledTicket = cancelTicket(ticketID);
            if (canceledTicket == null) {
                response.addErrorMessage("Error canceling Ticket, try again later!");
                logger.info("Error creating response for canceling Ticket!");
            } else if (canceledTicket.getDate() == null) {
                response.addErrorMessage("Time for canceling ticket has passed, ticket can be canceled 5 minutes after being played!");
                logger.info("Time for canceling ticket has passed!");
            } else if (canceledTicket.getState().equals(Constants.INVALID_DATA)) {
                response.addErrorMessage("Cannot cancel Ticket, please try again!");
                logger.info("Invalid Ticket state, unable to cancel ticket!");
            } else {
                response.setData(canceledTicket);
                logger.info("Successfully created response of cancelable Tickets!");
            }
        } catch (Exception e) {
            response.addErrorMessage("Error canceling Ticket, try again later!");
            logger.info("Error creating response for canceling Ticket!", e);
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