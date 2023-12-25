package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.List;


/**
 * Represents a service layer interface responsible for defining all Bet related methods.
 * Available API method implementations: GET
 *
 * @author Janko
 * @version 1.0
 */
public interface BetService {

    /**
     * Adds new bet to database. Returns instance of saved bet from database.
     *
     * @param bet instance of Bet class that is being saved.
     * @return instance of Bet class that is saved in database,
     * or null if error occurs.
     */
    Bet save(Bet bet);

    /**
     * Updates the state of all bets for fixtures that have finished.
     *
     * @throws Exception if there is an error while executing the update query.
     */
    void updateAllBets() throws Exception;

    /**
     * Adds list of Bet objects to database.
     *
     * @param betList list of Bet objects that is being saved.
     * @param ticket  instance of Ticket class that bet is associated with.
     * @throws Exception if Bet object inside list has invalid odds
     *                   or if an error occurs when saving Bet object
     */
    void saveBetsForTicket(List<Bet> betList, Ticket ticket) throws Exception;

    /**
     * Returns response for API call, containing list of bets that are associated with Ticket.
     *
     * @param ticketId Integer value representing id of Ticket that bets are contained in.
     * @return instance of ApiResponse class,
     * containing list of bets associated with Ticket,
     * or error message if operation fails.
     */
    ApiResponse<?> getBetsForTicketApiResponse(Integer ticketId);
}
