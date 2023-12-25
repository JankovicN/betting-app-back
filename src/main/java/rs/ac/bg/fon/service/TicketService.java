package rs.ac.bg.fon.service;

import org.springframework.data.domain.Pageable;
import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.utility.ApiResponse;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represents a service layer interface responsible for defining all Ticket related methods.
 * Available API method implementations: GET, POST, PATCH, DELETE
 *
 * @author Janko
 * @version 1.0
 */
public interface TicketService {

    /**
     * Updates the state of all bets and tickets for fixtures that have finished.
     * Returns response for API call.
     *
     * @return instance of ApiResponse class, containing messages regarding the success of the operation
     */
    ApiResponse<?> updateAllTickets();

    /**
     * Returns response for API call, containing tickets user has played.
     *
     * @param username String value representing username of user for which we are fetching tickets.
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing PageDTO that has an array of TicketBasicDTO in it,
     * or error message if operation fails.
     */
    ApiResponse<?> getUserTickets(String username, Pageable pageable);

    /**
     * Returns response for API call, containing tickets user has played for given date range.
     *
     * @param username      String value representing username of user for which we are fetching tickets.
     * @param startDateTime instance of LocalDateTime class that represents the start date of the range.
     * @param endDateTime   instance of LocalDateTime class that represents the end date of the range.
     * @param pageable      instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing PageDTO that has an array of TicketBasicDTO in it,
     * or error message if operation fails.
     */
    ApiResponse<?> getUserTickets(String username, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    /**
     * Fetches all tickets that have been played and places them in a page, then it transforms them to DTO.
     * Returns PageDTO containing list of TicketBasicDTO objects
     *
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing PageDTO that has an array of TicketBasicDTO in it,
     * or error message if operation fails.
     */
    ApiResponse<?> getAllTickets(Pageable pageable);

    /**
     * Fetches all tickets that have been played in a certain date range and places them in a page, then it transforms them to DTO.
     * Returns PageDTO containing list of TicketBasicDTO objects
     *
     * @param startDateTime instance of LocalDateTime class that represents the start date of the range.
     * @param endDateTime   instance of LocalDateTime class that represents the end date of the range.
     * @param pageable      instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing PageDTO that has an array of TicketBasicDTO in it,
     * or error message if operation fails.
     */
    ApiResponse<?> getAllTickets(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    /**
     * Updates all tickets that have been played in the last 5 minutes.
     * State changes from UNPROCESSED to PROCESSED.
     */
    void processTickets();

    /**
     * Transforms ticketDTO to ticket, saves the ticket and returns success message.
     * If ticketDTo has invalid fields or an error occurs an error message is returned.
     *
     * @param ticketDTO instance of TicketDTO class that represents ticket that we get from request body.
     * @return instance of ApiResponse class, containing messages regarding the success of the operation.
     */
    ApiResponse<?> addNewTicketApiResponse(TicketDTO ticketDTO);

    /**
     * Updates ticket state for tickets that have won and adds payment for those tickets.
     * State changes from WIN to PAYOUT.
     */
    void payoutUsers();

    /**
     * Fetches cancelable tickets for user, if username is provided.
     *
     * @param username instance of Optional<String>, representing username of user for which we are fetching tickets.
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing PageDTO that has an array of TicketCancelDTO in it,
     * or error message if operation fails.
     */
    ApiResponse<?> handleCancelTicketsAPI(Optional<String> username, Pageable pageable);

    /**
     * Deletes ticket from database based on its id, returns the deleted ticket.
     *
     * @param ticketID Integer value representing id of Ticket to be deleted.
     * @return instance of Ticket object that is deleted from database,
     * or null if error occurs.
     */
    Ticket cancelTicket(Integer ticketID);

    /**
     * Returns response for API call with messages indicating the success of the Ticket deletion from the database.
     *
     * @param ticketID Integer value representing id of Ticket to be deleted.
     * @return instance of ApiResponse class, containing messages regarding the success of the operation.
     */
    ApiResponse<?> cancelTicketApiResponse(Integer ticketID);

    /**
     * Returns response for API call containing tickets user has played within the specified date range, if provided.
     *
     * @param username String value representing username of user for which we are fetching tickets.
     * @param date     instance of Optional<String>, representing the date for which we are fetching tickets.
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, data for tickets played by user.
     */
    ApiResponse<?> handleGetUserTickets(String username, Optional<String> date, Pageable pageable);

    /**
     * Returns an API response containing all tickets played within the specified date range, if provided.
     *
     * @param date     instance of Optional<String>, representing the date for which we are fetching tickets.
     * @param pageable instance of Pageable class, contains information about the current page we are fetching.
     * @return instance of ApiResponse class, containing PageDTO that has an array of TicketBasicDTO in it,
     * or error message if operation fails.
     */
    ApiResponse<?> handleGetAllTickets(Optional<String> date, Pageable pageable);
}
