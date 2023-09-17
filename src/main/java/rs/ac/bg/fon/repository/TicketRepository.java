package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository  extends JpaRepository<Ticket, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE Ticket t\n" +
            "SET state = \n" +
            "    CASE \n" +
            "        WHEN EXISTS (\n" +
            "            SELECT 1 FROM Bet  WHERE ticket_id = t.id AND state = 'loss'\n" +
            "        ) THEN 'loss'\n" +
            "        WHEN NOT EXISTS (\n" +
            "            SELECT 1 FROM Bet WHERE ticket_id = t.id AND state <> 'win'\n" +
            "        ) THEN 'win'\n" +
            "        ELSE '-'\n" +
            "    END")
    void updateAllTickets();

    List<Ticket> findByUserUsername(String username);

    @Modifying
    @Query("UPDATE Ticket  SET state = 'processed' WHERE date < :date AND state = 'unprocessed'")
    void processTickets(@Param("date") LocalDateTime oldDate);

    @Query("SELECT SUM(wager) FROM Ticket WHERE user.id = :userId ")
    Double getWagerAmountForUser(@Param("userId") int userId);

    @Query("SELECT SUM(totalWin) FROM Ticket WHERE user.id = :userId AND state = 'win'")
    Double getTotalWinAmountForUser(@Param("userId") int userId);
}
