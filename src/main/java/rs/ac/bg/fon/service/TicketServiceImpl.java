package rs.ac.bg.fon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.mappers.BetMapper;
import rs.ac.bg.fon.mappers.TicketMapper;
import rs.ac.bg.fon.repository.TicketRepository;
import rs.ac.bg.fon.utility.ApiResponse;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;
    private TicketMapper ticketMapper;
    private BetMapper betMapper;

    private PaymentService paymentService;
    private UserService userService;
    private BetService betService;


    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void updateAllTickets() {
        ticketRepository.updateAllTickets();
    }

    @Override
    public List<Ticket> getUserTickets(String username) {
        return ticketRepository.findByUserUsername(username);
    }

    @Override
    public void processTickets() {
        LocalDateTime oldDate = LocalDateTime.now().minusMinutes(5);
        ticketRepository.processTickets(oldDate);
    }

    @Override
    public BigDecimal getWagerAmoutForUser(int userId) {
        return BigDecimal.valueOf(ticketRepository.getWagerAmountForUser(userId));
    }

    @Override
    public BigDecimal getTotalWinAmountForUser(int userId) {
        return BigDecimal.valueOf(ticketRepository.getTotalWinAmountForUser(userId));
    }
    @Transactional
    @Override
    public ApiResponse<?> addNewTicketApiResponse(TicketDTO ticketDTO) {

        ApiResponse<?> response = new ApiResponse<>();
        try {
            if(ticketDTO.getUsername()==null || ticketDTO.getUsername().isBlank()){
                response.addErrorMessage("Username is missing!");
            }else {
                User user = userService.getUser(ticketDTO.getUsername());
                List<Bet> betList = betMapper.betDTOListToBetList(ticketDTO.getBets());
                Ticket ticket = ticketMapper.ticketDTOToTicket(ticketDTO);

                if(paymentService.canUserPay(user.getId(), ticketDTO.getWager())){
                    setNewTicketFields(ticket);
                    ticket.setUser(user);

                    paymentService.addPayment(user.getId(), ticket.getWager().negate(), Constants.PAYMENT_WAGER);
                    ticketRepository.save(ticket);
                    betService.saveBetsForTicket(betList, ticket);
                }else{
                    response.addErrorMessage("Insufficient funds!");
                }
            }
        } catch (Exception e) {
            response.addErrorMessage(e.getMessage());
        }
        return response;
    }

    private void setNewTicketFields(Ticket ticket) {
        ticket.setDate(LocalDateTime.now());
        ticket.setState(Constants.TICKET_UNPROCESSED);
    }

    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    @Autowired
    public void setTicketMapper(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }
}