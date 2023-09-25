package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.constants.Constants;
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
            "            SELECT 1 FROM Bet  WHERE ticket_id = t.id AND state = '"+ Constants.BET_LOSS +"'\n" +
            "        ) THEN '"+Constants.TICKET_LOSS+"'\n" +
            "        WHEN NOT EXISTS (\n" +
            "            SELECT 1 FROM Bet WHERE ticket_id = t.id AND state <> '"+ Constants.BET_WIN +"'\n" +
            "        ) THEN '"+Constants.TICKET_WIN+"'\n" +
            "        ELSE '"+Constants.WAITING_FOR_RESULTS+"'\n" +
            "    END"+
            "  WHERE t.state='"+ Constants.WAITING_FOR_RESULTS +"' OR t.state='"+ Constants.TICKET_PROCESSED +"' ", nativeQuery = true)
    void updateAllTickets();

    List<Ticket> findByUserUsername(String username);

    @Modifying
    @Query("UPDATE Ticket  SET state = '"+Constants.TICKET_PROCESSED+"' WHERE date < :date AND state = '"+Constants.TICKET_UNPROCESSED+"'")
    void processTickets(@Param("date") LocalDateTime oldDate);

    @Query("SELECT SUM(wager) FROM Ticket WHERE user.id = :userId ")
    Double getWagerAmountForUser(@Param("userId") int userId);

    @Query("SELECT SUM(totalWin) FROM Ticket WHERE user.id = :userId AND state = '"+Constants.TICKET_WIN+"'")
    Double getTotalWinAmountForUser(@Param("userId") int userId);

    List<Ticket> findByState(String state);

    @Modifying
    @Query("SELECT * FROM Ticket WHERE date > :date")
    List<Ticket> getTicketsAfterDateTime(LocalDateTime cancelDateTime);

}
