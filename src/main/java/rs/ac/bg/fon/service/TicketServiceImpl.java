package rs.ac.bg.fon.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.mappers.TicketMapper;
import rs.ac.bg.fon.repository.TicketRepository;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;
    private TicketMapper ticketMapper;
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

    @Override
    public ApiResponse<?> addNewTicketApiResponse(TicketDTO ticketDTO) {

        //TODO add logic for adding ticket

        return null;
    }

    private void setNewTicketFields(Ticket ticket) {
        ticket.setDate(LocalDateTime.now());
        ticket.setState("-");
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