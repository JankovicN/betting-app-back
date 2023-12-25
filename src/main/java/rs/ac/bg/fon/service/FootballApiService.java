package rs.ac.bg.fon.service;

import rs.ac.bg.fon.utility.ApiResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a service layer interface responsible for defining all methods for external API calls.
 * Available API method implementations: POST
 *
 * @author Janko
 * @version 1.0
 */
public interface FootballApiService {

    /**
     * Asynchronously retrieves Odd Groups from an external Football API and processes the response.
     * Exceptions during the process are caught, and error messages are added to the ApiResponse.
     * The final ApiResponse is then completed and returned in the CompletableFuture.
     *
     * @return instance of CompletableFuture class representing the asynchronous completion of the operation,
     * containing an ApiResponse with details about the process outcome.
     */
    CompletableFuture<ApiResponse<?>> getOddGroupsFromAPI();

    /**
     * Asynchronously retrieves Odds from an external Football API and processes the response.
     * Exceptions during the process are caught, and error messages are added to the ApiResponse.
     * The final ApiResponse is then completed and returned in the CompletableFuture.
     *
     * @return instance of CompletableFuture class representing the asynchronous completion of the operation,
     * containing an ApiResponse with details about the process outcome.
     */
    CompletableFuture<ApiResponse<?>> getOddsFromAPI();

    /**
     * Asynchronously retrieves Fixtures from an external Football API and processes the response.
     * Exceptions during the process are caught, and error messages are added to the ApiResponse.
     * The final ApiResponse is then completed and returned in the CompletableFuture.
     *
     * @return instance of CompletableFuture class representing the asynchronous completion of the operation,
     * containing an ApiResponse with details about the process outcome.
     */
    CompletableFuture<ApiResponse<?>> getFixturesFromAPI();

    /**
     * Asynchronously retrieves fixtures and odds data from external Football APIs, combining the results into a single ApiResponse.
     * It uses thenCompose to chain the completion of the fixturesResult with the retrieval of odds data,
     * so that oddsResult is initiated only after fixturesResult is completed.
     * InfoMessages and ErrorMessages from both results are merged into one, creating a unified ApiResponse object.
     *
     * @return instance of CompletableFuture class representing the asynchronous completion of the operation,
     * containing an ApiResponse with merged details from both fixtures and odds responses.
     */
    CompletableFuture<ApiResponse<?>> getFixturesAndOddsFromAPI();
}
