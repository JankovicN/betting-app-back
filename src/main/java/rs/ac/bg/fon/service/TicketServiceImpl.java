package rs.ac.bg.fon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.repository.TicketRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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
    public void payoutTickets() {
        ticketRepository.payoutTickets();
    }

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
}