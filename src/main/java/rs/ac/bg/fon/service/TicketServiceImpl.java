package rs.ac.bg.fon.service;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.repository.TicketRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketServiceImpl implements TicketService{

    private TicketRepository ticketRepository;

    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

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
    public void proccessTickets() {
        LocalDateTime oldDate = LocalDateTime.now().minusMinutes(5);
        ticketRepository.proccessTickets(oldDate);
    }


    @Override
    public Double getWagerAmoutForUser(String username) {
        return ticketRepository.getWagerAmoutForUser(username);
    }

    @Override
    public Double getTotalWinAmountForUser(String username) {
        return ticketRepository.getTotalWinAmountForUser(username);
    }

    @Override
    public void cancelTicket(int ticketId) {
        ticketRepository.cancelTicket(ticketId);
    }

    @Override
    public Optional<Ticket> getTicketById(int ticketId) {
        return ticketRepository.findById(ticketId);
    }



    @Override
    public Ticket createTicketFromJsonString(String ticketJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement responseEl = gson.fromJson(ticketJson, JsonElement.class);

        JsonObject ticketObject = responseEl.getAsJsonObject().get("ticket").getAsJsonObject();
        String wagerStr = ticketObject.get("wager").toString();
        double totalOdds = ticketObject.get("totalOdds").getAsDouble();
        String payoutStr = ticketObject.get("payout").toString();
        BigDecimal payout = new BigDecimal(payoutStr);
        BigDecimal wager = new BigDecimal(wagerStr);


        Ticket ticket = new Ticket();
        ticket.setWager(wager);
        ticket.setOdd(totalOdds);
        ticket.setTotalWin(payout);
        ticket.setDate(LocalDateTime.now());

        return ticket;
    }
}