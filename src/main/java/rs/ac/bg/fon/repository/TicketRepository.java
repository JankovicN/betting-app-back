package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.bg.fon.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository  extends JpaRepository<Ticket, Integer> {
    @Modifying
    @Query(value = "UPDATE ticket\n" +
            "SET state = \n" +
            "    CASE \n" +
            "        WHEN EXISTS (\n" +
            "            SELECT 1 FROM bet  WHERE ticket_id = ticket.ticket_id AND state = 'loss'\n" +
            "        ) THEN 'loss'\n" +
            "        WHEN NOT EXISTS (\n" +
            "            SELECT 1 FROM bet WHERE ticket_id = ticket.ticket_id AND state <> 'win'\n" +
            "        ) THEN 'win'\n" +
            "        ELSE '-'\n" +
            "    END")
    void updateAllTickets();

    List<Ticket> findByUserUsername(String username);

    @Modifying
    @Query("UPDATE ticket  SET state = 'processed' WHERE e.date < :oldDate AND state = 'unprocessed' ")
    void proccessTickets(@Param("oldDate") LocalDateTime oldDate);

    @Query("SELECT SUM(wager) FROM Payment WHERE user_id = :userId")
    Double getWagerAmoutForUser(int userId);

    @Query("SELECT SUM(total_win) FROM Payment WHERE user_id = :userId AND state = 'win'")
    Double getTotalWinAmountForUser(int userId);
}
