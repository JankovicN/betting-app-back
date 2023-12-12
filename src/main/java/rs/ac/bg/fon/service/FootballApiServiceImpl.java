package rs.ac.bg.fon.service;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.constants.SecretKeys;
import rs.ac.bg.fon.entity.*;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.JsonValidation;
import rs.ac.bg.fon.utility.Utility;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a service layer class responsible for implementing all logic for interacting with FootballAPI.
 * Available API method implementations: POST
 *
 * @author Janko
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FootballApiServiceImpl implements FootballApiService {

    /**
     * Instance of Logger class, responsible for displaying messages that contain information about the success of methods inside FootballApi service class.
     */
    private static final Logger logger = LoggerFactory.getLogger(FootballApiServiceImpl.class);

    /**
     * Instance of League service class, responsible for executing any logic related to League entity.
     */
    private final LeagueService leagueService;

    /**
     * Instance of Fixture service class, responsible for executing any logic related to Fixture entity.
     */
    private final FixtureService fixtureService;

    /**
     * Instance of Odd Group service class, responsible for executing any logic related to OddGroup entity.
     */
    private final OddGroupService oddGroupService;

    /**
     * Instance of Team service class, responsible for executing any logic related to Team entity.
     */
    private final TeamService teamService;

    /**
     * Instance of Odd service class, responsible for executing any logic related to Odd entity.
     */
    private final OddService oddService;

    /**
     * Instance of Gson class, responsible for handling JSON document.
     */
    private final Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Returns array of LocalDateTime objects for range of dates specified.
     *
     * @param start instance of LocalDateTime class that represents the start date of the range.
     * @param end instance of LocalDateTime class that represents the end date of the range.
     * @return Returns array of LocalDateTime objects, from start date time to end date time.
     *
     */
    private LocalDateTime[] getDateRange(LocalDateTime start, LocalDateTime end) {
        long numDays = ChronoUnit.DAYS.between(start, end);
        LocalDateTime[] dates = new LocalDateTime[(int) numDays];
        for (int i = 0; i < numDays; i++) {
            dates[i] = start.plusDays(i);
        }
        return dates;
    }

    /**s
     * Checks if the odd name provided has 2 strings, separated by whitespace and the second part of string ends with '.5'.
     *
     * @param oddName String value representing the name of odd.
     * @return boolean value, return true if String is in right format,
     *         otherwise return false.
     *
     */
    private boolean isFormatNumberDotFive(String oddName) {
        String[] parts = oddName.split(" ");
        if (parts.length == 2) {
            String numberPart = parts[1];
            return numberPart.matches("\\d+\\.5");
        }
        return false;
    }

    /**
     * Asynchronously retrieves Odd Groups from an external Football API and processes the response.
     * Exceptions during the process are caught, and error messages are added to the ApiResponse.
     * The final ApiResponse is then completed and returned in the CompletableFuture.
     *
     * @return instance of CompletableFuture class representing the asynchronous completion of the operation,
     *         containing an ApiResponse with details about the process outcome.
     *
     */
    @Transactional
    @Override
    @Async
    public CompletableFuture<ApiResponse<?>> getOddGroupsFromAPI() {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            if (oddGroupService.exists()) {
                try {
                    String responseBody = oddGroupsApiResponse();

                    if (responseBody == null || responseBody.isEmpty()) {
                        logger.error("Invalid response from external API!\nFootballAPI OddGroups call returned empty response!");
                        apiResponse.addErrorMessage("Invalid response from external API!");
                        return CompletableFuture.completedFuture(apiResponse);
                    }

                    JsonArray responseArray = getResponseArrayFromJson(responseBody);

                    if (responseArray == null) {
                        logger.info("No Odd Groups were added!\nFootballAPI OddGroups call returned empty array!");
                        apiResponse.addInfoMessage("No Odd Groups were added!");
                        return CompletableFuture.completedFuture(apiResponse);
                    }

                    for (JsonElement jsonElement : responseArray) {
                        OddGroup group = createOddGroupFromJsonElement(jsonElement);
                        if (oddGroupService.saveOddGroup(group) == null) {
                            logger.warn("Error while saving Odd Group " + group + "!");
                        } else {
                            apiResponse.addInfoMessage("Successfully added new Odd Group " + group + "!");
                        }
                    }
                    logger.info("Successful added Odd Groups!");
                } catch (IOException e) {
                    apiResponse.addErrorMessage("Error getting Odd Groups from Football API!");
                    logger.error("FootballAPI Odd Groups Error transforming response to Odd Group!", e);
                } catch (Exception e) {
                    apiResponse.addErrorMessage("Error getting Odd Groups from Football API!");
                    logger.error("FootballAPI OddGroups Error when getting Odd Groups!", e);
                }
            } else {
                logger.info("Odd Groups are already fetched!");
                apiResponse.addInfoMessage("No new Odd Groups were added!");
            }
        } catch (Exception e) {
            apiResponse.addErrorMessage("Error getting Odd Groups from Football API");
            logger.error("FootballAPI Odd Group Error when getting Odd Groups", e);
        }
        return CompletableFuture.completedFuture(apiResponse);
    }


    /**
     * Asynchronously retrieves Odds from an external Football API and processes the response.
     * Exceptions during the process are caught, and error messages are added to the ApiResponse.
     * The final ApiResponse is then completed and returned in the CompletableFuture.
     *
     * @return instance of CompletableFuture class representing the asynchronous completion of the operation,
     *         containing an ApiResponse with details about the process outcome.
     *
     */
    @Override
    public CompletableFuture<ApiResponse<?>> getOddsFromAPI() {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            LocalDateTime[] dateRange = getDateRange(currentDateAddOffset(0), currentDateAddOffset(3));
            for (LocalDateTime localDateTime : dateRange) {
                List<League> leagueList = leagueService.getAllLeagues();
                if (leagueList == null) {
                    logger.warn("List of Leagues is empty!");
                    continue;
                }
                for (League league : leagueList) {
                    try {
                        try {
                            Thread.sleep(1000); // 1000 milliseconds (1 second) delay
                        } catch (InterruptedException e) {
                            logger.error("Error while thread was asleep: {}", e);
                        }
                        String responseBody = oddsApiCall(league.getId(), Utility.formatDate(localDateTime));
                        if (responseBody == null || responseBody.isEmpty()) {
                            logger.error("Invalid response from external API!\nFootballAPI Odds call returned empty response!\n" + responseBody);
                            apiResponse.addErrorMessage("Invalid response from external API!");
                            continue;
                        }

                        JsonArray jsonArr = getResponseArrayFromJson(responseBody);
                        if (jsonArr == null) {
                            logger.warn("No Odds were added!\nFootballAPI Odds call response field is empty!\n" + jsonArr);
                            apiResponse.addInfoMessage("No Odds were added, for Date = " + Utility.formatDate(localDateTime) + " and League = " + league + "!");
                            continue;
                        }

                        for (JsonElement jsonElement : jsonArr) {
                            addOddFromApiResponse(jsonElement);
                        }
                        apiResponse.addInfoMessage("Successfully added new Odds, for Date = " + Utility.formatDate(localDateTime) + " and League = " + league + "!");
                        logger.info("Successful FootballAPI Odds call for getting Odds, for Date = " + Utility.formatDate(localDateTime) + " and League = " + league + "!");
                    } catch (IOException e) {
                        apiResponse.addErrorMessage("Error getting Odds from Football API , for Date = " + Utility.formatDate(localDateTime) + " and League = " + league + "!");
                        logger.error("IOException: FootballAPI Odds Error transforming response to Odd, for Date = " + Utility.formatDate(localDateTime) + "and League = " + league + "!");
                    } catch (Exception e) {
                        apiResponse.addErrorMessage("Error getting Odds from Football API, for Date = " + Utility.formatDate(localDateTime) + " and League = " + league + "!");
                        logger.error("FootballAPI Odds Error when getting Odds, for Date = " + Utility.formatDate(localDateTime) + " and League = " + league + "!", e);
                    }
                }
            }
        } catch (Exception e) {
            apiResponse.addErrorMessage("Error getting Odds from Football API");
            logger.error("FootballAPI Odds Error when getting all Odds", e);
        }
        updateInfoMessages(apiResponse, "No Odds were added", "No new Odds were added");
        return CompletableFuture.completedFuture(apiResponse);
    }

    /**
     * Asynchronously retrieves Fixtures from an external Football API and processes the response.
     * Exceptions during the process are caught, and error messages are added to the ApiResponse.
     * The final ApiResponse is then completed and returned in the CompletableFuture.
     *
     * @return instance of CompletableFuture class representing the asynchronous completion of the operation,
     *         containing an ApiResponse with details about the process outcome.
     *
     */
    @Override
    public CompletableFuture<ApiResponse<?>> getFixturesFromAPI() {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            String dateFromString = currentDateAddOffsetInFormat(-1);
            String dateToString = currentDateAddOffsetInFormat(3);
            List<League> leagueList = leagueService.getAllLeagues();
            if (leagueList == null) {
                logger.warn("List of Leagues is empty!");
                apiResponse.addInfoMessage("No Leagues found!\nContact support for more information!");
                return CompletableFuture.completedFuture(apiResponse);
            }
            for (League league : leagueList) {
                try {
                    try {
                        Thread.sleep(1000); // 1000 milliseconds (1 second) delay
                    } catch (InterruptedException e) {
                        logger.error("Error while thread was asleep: {}", e);
                    }
                    String responseBody = fixturesApiCall(league.getId(), dateFromString, dateToString);
                    if (responseBody == null || responseBody.isEmpty()) {
                        logger.error("Invalid response from external API!\nFootballAPI Fixtures call returned empty response!\n" + responseBody);
                        apiResponse.addErrorMessage("Invalid response from external API!");
                        continue;
                    }

                    JsonArray jsonArr = getResponseArrayFromJson(responseBody);
                    if (jsonArr == null) {
                        logger.info("No Fixtures were added!\nFootballAPI Fixtures call response field is empty!\n" + jsonArr);
                        apiResponse.addInfoMessage("No Fixtures were added for league " + league + "!");
                        continue;
                    }

                    for (JsonElement jsonElement : jsonArr) {
                        addFixtureFromApiResponse(jsonElement, league, apiResponse);

                    }
                    apiResponse.addInfoMessage("Successfully added new fixtures for League " + league + "!");
                    logger.info("Successful FootballAPI Fixtures call for getting fixtures");
                } catch (IOException e) {
                    apiResponse.addErrorMessage("Error getting new fixtures for League = " + league + "!");
                    logger.error("FootballAPI Fixtures Error transforming response to Fixtures for League = " + league, e);
                } catch (Exception e) {
                    apiResponse.addErrorMessage("Error getting new fixtures for League = " + league + "!");
                    logger.error("FootballAPI Fixtures Error when getting fixtures", e);
                }
            }
        } catch (Exception e) {
            apiResponse.addErrorMessage("Error getting fixtures from Football API");
            logger.error("FootballAPI Fixtures Error when getting fixtures", e);
        }
        updateInfoMessages(apiResponse, "No Fixtures were added", "No new Fixtures were added");
        return CompletableFuture.completedFuture(apiResponse);
    }

    /**
     * Asynchronously retrieves fixtures and odds data from external Football APIs, combining the results into a single ApiResponse.
     * It uses thenCompose to chain the completion of the fixturesResult with the retrieval of odds data,
     * so that oddsResult is initiated only after fixturesResult is completed.
     * InfoMessages and ErrorMessages from both results are merged into one, creating a unified ApiResponse object.
     *
     * @return instance of CompletableFuture class representing the asynchronous completion of the operation,
     *         containing an ApiResponse with merged details from both fixtures and odds responses.
     *
     */
    @Override
    public CompletableFuture<ApiResponse<?>> getFixturesAndOddsFromAPI() {
        CompletableFuture<ApiResponse<?>> fixturesResult = getFixturesFromAPI();

        // Use thenCompose to wait for the completion of the fixturesResult before starting the oddsResult
        CompletableFuture<ApiResponse<?>> combinedResult =
                fixturesResult.thenCompose(fixturesApiResponse -> {
                    CompletableFuture<ApiResponse<?>> oddsResult = getOddsFromAPI();

                    // Merge the data from both ApiResponse objects
                    return oddsResult.thenApply(oddsApiResponse -> {
                        System.out.println(oddsApiResponse.getInfoMessages());
                        System.out.println(fixturesApiResponse.getInfoMessages());
                        fixturesApiResponse.getInfoMessages().addAll(oddsApiResponse.getInfoMessages());
                        fixturesApiResponse.getErrorMessages().addAll(oddsApiResponse.getErrorMessages());
                        return fixturesApiResponse;
                    });
                });

        // Return a new CompletableFuture with additional processing using ApiResponseUtil
        return combinedResult;
    }

    /**
     * Checks if there are any odds that are associated with fixture and odd group provided.
     *
     * @param fixtureID Integer value representing id of fixture.
     * @param oddGroupID Integer value representing id of odd group.
     * @return boolean value, return true if there are any odds for fixture and odd group,
     *         otherwise return false.
     *
     */
    @Override
    public ApiResponse<?> exists(Integer fixtureID, Integer oddGroupID) {
        ApiResponse<Boolean> response = new ApiResponse<>();
        response.setData(oddService.existsWithFixtureIdAndOddGroupId(fixtureID, oddGroupID));
        return response;
    }

    /**
     * Creates fixture from JSON element and saves it to database.
     * If errors occur they are logged.
     *
     * @param jsonElement instance of JsonElement class, that contains data  related to fixture.
     * @param league instance of League class, that fixture is in.
     * @param apiResponse instance of ApiResponse class, that contains error message if error occurs.
     *
     */
    @Transactional
    private void addFixtureFromApiResponse(JsonElement jsonElement, League league, ApiResponse<?> apiResponse) {

        // Checking if JSON field are present and if it's a JSON Object
        if (!JsonValidation.validateJsonElementFieldIsObject(jsonElement, "teams")) {
            return;
        }

        // Checking if JSON fields are present and if they are JSON Objects
        JsonElement teams = jsonElement.getAsJsonObject().get("teams");
        if (!JsonValidation.validateJsonElementFieldIsObject(teams, "home")
                || !JsonValidation.validateJsonElementFieldIsObject(teams, "away")) {
            return;
        }

        // Creating home/away team from JSON
        Team home = createTeamFromJsonElement(teams.getAsJsonObject().get("home"));
        Team away = createTeamFromJsonElement(teams.getAsJsonObject().get("away"));
        // Check if home/away is correctly created
        if (home == null || away == null) {
            logger.warn("Missing home [" + home + "] or away [" + away + "] team in API response!");
            return;
        }

        // Check if home/away is saved correctly
        if (teamService.save(home) == null || teamService.save(away) == null) {
            logger.warn("Error while saving home/away team!");
            return;
        }

        // Creating fixture team from JSON
        Fixture fixture = createFixtureFromJsonElement(jsonElement);
        if (fixture == null) {
            return;
        }
        fixture.setOdds(new ArrayList<>());
        fixture.setHome(home);
        fixture.setAway(away);
        fixture.setLeague(league);
//
//        home.getHome().add(fixture);
//        away.getAway().add(fixture);
//        league.getFixtures().add(fixture);

        if (fixtureService.save(fixture) == null) {
            logger.info("Error while trying to save Fixture " + fixture + "!");
            apiResponse.addInfoMessage("Error while trying to save Fixture " + fixture + "!");
        }
    }

    /**
     * Creates list of odds from JSON element and saves them to database.
     * If errors occur they are logged.
     *
     * @param jsonElement instance of JsonElement class, that contains data  related to odds.
     *
     */
    @Transactional
    private void addOddFromApiResponse(JsonElement jsonElement) {
        // Checking if JSON field are present and of correct type
        if (!JsonValidation.validateJsonElementFieldIsObject(jsonElement, "fixture")
                || !JsonValidation.validateJsonElementFieldIsNumber(jsonElement.getAsJsonObject().get("fixture"), "id")) {
            logger.warn("addOddFromApiResponse: Missing fixture or id field in JSON!\n" + jsonElement);
            return;
        }
        // Get fixture by ID that we read from JSON
        Fixture fixture = fixtureService.getFixtureById(jsonElement.getAsJsonObject().get("fixture").getAsJsonObject().get("id").getAsInt());
        // Validate that fixture is fetched correctly and that it hasn't started yet
        if (fixture == null || !fixture.getState().equals(Constants.FIXTURE_NOT_STARTED)) {
            logger.warn("addOddFromApiResponse: Invalid Fixture!\n" + fixture);
            return;
        }

        JsonArray betsArray = getBetsArrayFromBookmakerJsonElement(jsonElement);
        if (betsArray == null) {
            logger.warn("addOddFromApiResponse: Bet Array is null!\n");
            return;
        }
        for (JsonElement oddGroupEl : betsArray) {

            // Checking if JSON field are present and of correct type
            if (!JsonValidation.validateJsonElementFieldIsNumber(oddGroupEl, "id")
                    || !JsonValidation.validateJsonElementFieldIsArray(oddGroupEl, "values")) {
                continue;
            }

            // Getting Odd Group id from JSON
            Integer oddGroupId = oddGroupEl.getAsJsonObject().get("id").getAsInt();
            // If we don't have that Odd Group or if odds are already saved fo that fixture and Odd Group then return
            if (!oddGroupService.existsWithId(oddGroupId)
                    || oddService.existsWithFixtureIdAndOddGroupId(fixture.getId(), oddGroupId)) {
                continue;
            }

            // Get Odd Group with ID
            // If oddGroup is null it means that an error has occurred, that is why we return
            OddGroup oddGroup = oddGroupService.getOddGroupWithId(oddGroupId);
            if (oddGroup == null) {
                continue;
            }

            // Create list of odds, go through JSON and create odds from it
            // Then add those odds to list
            // Log if error has occurred while creating odd
            List<Odd> oddList = new ArrayList<>();
            JsonArray oddsArr = oddGroupEl.getAsJsonObject().get("values").getAsJsonArray();
            for (JsonElement oddsEl : oddsArr) {
                Odd odd = crateOddFromJsonElement(oddsEl);
                if (odd == null) {
                    logger.warn("addOddFromApiResponse: Error while trying to crate Odd from Json \n" + oddsEl);
                    continue;
                } else {
                    String oddName = odd.getName();
                    if ((oddName.contains("Over")
                            || oddName.contains("Under"))
                            && !isFormatNumberDotFive(oddName)) {
                        logger.warn("addOddFromApiResponse: Removing odd that has wrong format, odd value = " + odd.getOddValue() + " \n" + oddsEl);
                        continue;
                    }
                }
                odd.setFixture(fixture);
                odd.setOddGroup(oddGroup);
                oddList.add(odd);
                logger.info("addOddFromApiResponse: Adding odd to list " + odd.getName() + " for fixture " + fixture.getHome().getName() + " - " + fixture.getAway().getName());
            }

            // Save all odds that we created
            // Log if error has occurred
            if (oddService.saveOddList(oddList).isEmpty()) {
                logger.warn("addOddFromApiResponse: Error while trying to save Odd List!");
            } else {
                logger.info("addOddFromApiResponse: Saving odd list for Odd Group " + oddGroup.getName());
            }

        }
    }

    /**
     * Creates fixture from JSON element.
     * If errors occur they are logged.
     *
     * @param jsonElement instance of JsonElement class, that contains data related to the fixture.
     * @return instance of Fixture class created from JSON element.
     */
    @Transactional
    private Fixture createFixtureFromJsonElement(JsonElement jsonElement) {
        try {
            if (!JsonValidation.validateJsonElementFieldIsObject(jsonElement, "fixture")
                    || !JsonValidation.validateJsonElementFieldIsObject(jsonElement, "goals")) {
                return null;
            }

            JsonElement fixtureElement = jsonElement.getAsJsonObject().get("fixture");

            if (!JsonValidation.validateJsonElementFieldIsNumber(fixtureElement, "id")
                    || !JsonValidation.validateJsonElementFieldIsString(fixtureElement, "date")
                    || !JsonValidation.validateJsonElementFieldIsObject(fixtureElement, "status")
                    || !JsonValidation.validateJsonElementFieldIsString(fixtureElement.getAsJsonObject().get("status"), "short")) {
                return null;
            }

            // Get fields from JSON
            JsonObject goals = jsonElement.getAsJsonObject().get("goals").getAsJsonObject();
            int homeGoals = getTeamGoalsFromJsonElement(goals.get("home"));
            int awayGoals = getTeamGoalsFromJsonElement(goals.get("away"));
            Integer id = fixtureElement.getAsJsonObject().get("id").getAsInt();
            String status = fixtureElement.getAsJsonObject().get("status").getAsJsonObject().get("short").getAsString();
            String date = fixtureElement.getAsJsonObject().get("date").getAsString();
            date = date.substring(0, date.indexOf("+")).replace("T", " ");
            LocalDateTime formattedDate = Utility.parseDateTime(date);

            // Creating fixture and returning it
            Fixture fixture = new Fixture();
            fixture.setId(id);
            fixture.setDate(formattedDate);
            fixture.setState(status);
            fixture.setHomeGoals(homeGoals);
            fixture.setAwayGoals(awayGoals);
            return fixture;

        } catch (Exception e) {
            logger.error("Exception: FootballAPI Fixtures Error parsing fixture json\n" + e);
        }
        return null;

    }

    /**
     * Executes API call for fetching odds, for the provided date and league.
     *
     * @param leagueId int value representing id of league.
     * @param date String value representing date in format yyyy-MM-dd.
     * @return String value representing body of API response, containing data related to odds.
     */
    private String oddsApiCall(int leagueId, String date) throws IOException, InterruptedException {
        logger.info("FootballAPI Odds call for getting odds for league");
        String uriString = "https://api-football-v1.p.rapidapi.com/v3/odds?league=" + leagueId + "&season=" + Constants.SEASON + "&date=" + date;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriString))
                .header("X-RapidAPI-Key", SecretKeys.getApi_key())
                .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful FootballAPI Odds call for getting odds for league");
        return response.body();
    }

    /**
     * Executes API call for fetching fixtures, for the provided date range and league.
     *
     * @param leagueId int value representing id of league.
     * @param dateFrom String value representing date, initial date in the range, in format yyyy-MM-dd.
     * @param dateTo String value representing date, final date in the range, in format yyyy-MM-dd.
     * @return String value representing body of API response, containing data related to fixtures.
     */
    private String fixturesApiCall(int leagueId, String dateFrom, String dateTo) throws IOException, InterruptedException {
        logger.info("FootballAPI Fixtures call for getting fixtures");
        String uriString = "https://api-football-v1.p.rapidapi.com/v3/fixtures?league=" + leagueId + "&season=" + Constants.SEASON + "&from=" + dateFrom + "&to=" + dateTo + "&timezone=Europe%2FBelgrade";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriString))
                .header("X-RapidAPI-Key", SecretKeys.getApi_key())
                .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful Fixtures call for getting fixtures");
        return response.body();
    }

    /**
     * Executes API call for fetching all odd groups.
     *
     * @return String value representing body of API response, containing data related to odd groups.
     */
    private String oddGroupsApiResponse() throws IOException, InterruptedException {
        logger.info("Creating Football API call for getting Odd Groups.");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api-football-v1.p.rapidapi.com/v3/odds/bets"))
                .header("X-RapidAPI-Key", SecretKeys.getApi_key())
                .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful Football API call for getting Odd Groups");
        return response.body();
    }

    /**
     * Creates JsonArray from JsonElement, containing odds created by Bet365.
     *
     * @param jsonElement instance of JsonElement class, that contains data related to odds by bookmaker.
     * @return instance of JsonArray class, related to odds created by Bet365,
     *         or null if there are no odds made by Bet365.
     */
    private JsonArray getBetsArrayFromBookmakerJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementFieldIsArray(jsonElement, "bookmakers")) {
            return null;
        }
        JsonArray bookmakersArr = jsonElement.getAsJsonObject().get("bookmakers").getAsJsonArray();

        for (JsonElement bookmaker : bookmakersArr) {
            if (!JsonValidation.validateJsonElementFieldIsNumber(bookmaker, "id")
                    || !JsonValidation.validateJsonElementFieldIsArray(bookmaker, "bets")) {
                continue;
            }

            int idElement = bookmaker.getAsJsonObject().get("id").getAsInt();
            JsonElement betsArrayElement = bookmaker.getAsJsonObject().get("bets");
            if (idElement != Constants.BET_365_ID) {
                continue;
            }

            return betsArrayElement.getAsJsonArray();
        }
        return null;
    }

    /**
     * Creates JsonArray from String value representing JSON body from API response.
     *
     * @param responseBody String value representing JSON body from API response.
     * @return instance of JsonArray class, that represents response field in API response,
     *         or null if there is no response field in API response.
     */
    private JsonArray getResponseArrayFromJson(String responseBody) {
        JsonElement responseEl = gsonBuilder.fromJson(responseBody, JsonElement.class);
        if (!JsonValidation.validateJsonElementFieldIsArray(responseEl, "response")) {
            logger.info("Error getting response body\n" + responseBody);
            return null;
        }
        return responseEl.getAsJsonObject().getAsJsonArray("response");
    }

    /**
     * Creates odd group from JSON element.
     *
     * @param jsonElement instance of JsonElement class, that contains data related to odd group.
     * @return instance of OddGroup class created from JSON element
     *         or null if there json element fields are invalid.
     */
    private OddGroup createOddGroupFromJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementFieldIsNumber(jsonElement, "id")
                || !JsonValidation.validateJsonElementFieldIsString(jsonElement, "name")) {
            return null;
        }
        int oddGroupID = jsonElement.getAsJsonObject().get("id").getAsInt();
        String oddGroupName = jsonElement.getAsJsonObject().get("name").getAsString();
        return new OddGroup(oddGroupID, oddGroupName, new ArrayList<>());
    }

    /**
     * Extracts number of goals from Json Element.
     *
     * @param jsonElement instance of JsonElement class, that contains data related to number of goals.
     * @return int value representing number of goals extracted from JSON element
     *         or 0 if json element is invalid.
     */
    private int getTeamGoalsFromJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementIsNumber(jsonElement)) {
            logger.warn("Goal value is invalid [" + jsonElement + "]!");
            return 0;
        }
        return jsonElement.getAsInt();
    }

    /**
     * Creates team from JSON element.
     *
     * @param jsonElement instance of JsonElement class, that contains data related to team.
     * @return instance of Team class created from JSON element
     *         or null if there json element fields are invalid.
     */
    private Team createTeamFromJsonElement(JsonElement jsonElement) {
        JsonObject responseObject = jsonElement.getAsJsonObject();
        if (!responseObject.has("id")
                || !responseObject.has("name")) {
            return null;
        }

        JsonElement idElement = responseObject.get("id");
        JsonElement nameElement = responseObject.get("name");
        if (!idElement.isJsonPrimitive()
                || !idElement.getAsJsonPrimitive().isNumber()
                || !nameElement.isJsonPrimitive()
                || !nameElement.getAsJsonPrimitive().isString()) {
            return null;
        }
        Team team = new Team();
        team.setId(idElement.getAsInt());
        team.setName(nameElement.getAsString());
        return team;
    }

    /**
     * Creates odd from JSON element.
     *
     * @param jsonElement instance of JsonElement class, that contains data related to odd.
     * @return instance of Odd class created from JSON element
     *         or null if there json element fields are invalid.
     */
    private Odd crateOddFromJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementFieldIsString(jsonElement, "odd")
                || !JsonValidation.validateJsonElementFieldIsString(jsonElement, "value")) {
            return null;
        }

        String oddValueString = jsonElement.getAsJsonObject().get("odd").getAsString();
        BigDecimal oddValue = BigDecimal.valueOf(Double.parseDouble(oddValueString));
        String oddName = jsonElement.getAsJsonObject().get("value").getAsString();

        Odd odd = new Odd();
        odd.setOddValue(oddValue);
        odd.setName(oddName);
        return odd;
    }

    /**
     * Returns String that represents date, that is offset by number of days provided, in format 'yyyy-MM-dd'.
     *
     * @param days long value representing the number of days that current date is offset by.
     * @return String value that represents date current offset by number of days.
     */
    private String currentDateAddOffsetInFormat(long days) {
        return Utility.formatDate(LocalDateTime.now().plusDays(days));
    }

    /**
     * Returns LocalDateTime date, that is offset by number of days provided.
     *
     * @param days long value representing the number of days that current date is offset by.
     * @return instance of LocalDateTime, that represents current date offset by number of days.
     */
    private LocalDateTime currentDateAddOffset(long days) {
        return LocalDateTime.now().plusDays(days);
    }

    /**
     * Updates infoMessages in ApiResponse object,
     * if they all contain the String value provided, then replace them with a new message.
     *
     * @param apiResponse instance of ApiResponse class that is to be updated.
     * @param containsString String value to be checked for in infoMessages.
     * @param newMessage String value that replaces infoMessages.
     */
    private void updateInfoMessages(ApiResponse<?> apiResponse, String containsString, String newMessage) {
        List<String> infoMessages = apiResponse.getInfoMessages();
        boolean noFixturesAdded = infoMessages.stream().allMatch(s -> s.contains(containsString));
        if (noFixturesAdded) {
            List<String> newInfoMessages = new ArrayList<>();
            newInfoMessages.add(newMessage);
            apiResponse.setInfoMessages(newInfoMessages);
        }
    }

}


