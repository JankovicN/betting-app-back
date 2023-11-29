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

@Service
@RequiredArgsConstructor
@Transactional
public class FootballApiServiceImpl implements FootballApiService {
    private static final Logger logger = LoggerFactory.getLogger(FootballApiServiceImpl.class);
    private final LeagueService leagueService;
    private final FixtureService fixtureService;
    private final OddGroupService oddGroupService;
    private final TeamService teamService;
    private final OddService oddService;
    private final Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

    private LocalDateTime[] getDateRange(LocalDateTime start, LocalDateTime end) {
        long numDays = ChronoUnit.DAYS.between(start, end);
        LocalDateTime[] dates = new LocalDateTime[(int) numDays];
        for (int i = 0; i < numDays; i++) {
            dates[i] = start.plusDays(i);
        }
        return dates;
    }

    private boolean isFormatNumberDotFive(String oddName) {
        String[] parts = oddName.split(" ");
        if (parts.length == 2) {
            String numberPart = parts[1];
            return numberPart.matches("\\d+\\.5");
        }
        return false;
    }

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

    @Override
    public ApiResponse<?> exists(Integer fixtureID, Integer oddGroupID) {
        ApiResponse<Boolean> response = new ApiResponse<>();
        response.setData(oddService.existsWithFixtureIdAndOddGroupId(fixtureID, oddGroupID));
        return response;
    }

    @Transactional
    private void addFixtureFromApiResponse(JsonElement jsonElement, League league, ApiResponse<?> apiResponse) {

        // Checking if JSON field are present and if it's a JSON Object
        if (!JsonValidation.validateJsonElementFieldIsObject(jsonElement, "teams")) {
            return;
        }

        // Checking if JSON fields are present and if they are JSON Objects
        JsonElement teams = jsonElement.getAsJsonObject().get("teams");
        if (!JsonValidation.validateJsonElementFieldIsObject(teams, "home") || !JsonValidation.validateJsonElementFieldIsObject(teams, "away")) {
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

    @Transactional
    private void addOddFromApiResponse(JsonElement jsonElement) {
        // Checking if JSON field are present and of correct type
        if (!JsonValidation.validateJsonElementFieldIsObject(jsonElement, "fixture") || !JsonValidation.validateJsonElementFieldIsNumber(jsonElement.getAsJsonObject().get("fixture"), "id")) {
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
            if (!JsonValidation.validateJsonElementFieldIsNumber(oddGroupEl, "id") || !JsonValidation.validateJsonElementFieldIsArray(oddGroupEl, "values")) {
                continue;
            }

            // Getting Odd Group id from JSON
            Integer oddGroupId = oddGroupEl.getAsJsonObject().get("id").getAsInt();
            // If we don't have that Odd Group or if odds are already saved fo that fixture and Odd Group then return
            if (!oddGroupService.existsWithId(oddGroupId) || oddService.existsWithFixtureIdAndOddGroupId(fixture.getId(), oddGroupId)) {
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
                String oddName = odd.getName();
                if (odd == null) {
                    logger.warn("addOddFromApiResponse: Error while trying to crate Odd from Json \n" + oddsEl);
                    continue;
                } else if ((oddName.contains("Over") || oddName.contains("Under")) && !isFormatNumberDotFive(oddName)) {
                    logger.warn("addOddFromApiResponse: Removing odd that has wrong format, odd value = " + odd.getOdd() + " \n" + oddsEl);
                    continue;
                }
                odd.setFixture(fixture);
                odd.setOddGroup(oddGroup);
                oddList.add(odd);
                logger.info("addOddFromApiResponse: Adding odd to list " + odd.getName() + " for fixture " + fixture.getHome().getName() + " - " + fixture.getAway().getName());
            }

            List<Odd> previousOdds = oddService.getOddsForFixtureAndOddGroup(fixture.getId(), oddGroupId);
            // Save all odds that we created
            // Log if error has occurred
            if (oddService.saveOddList(oddList) == null) {
                logger.warn("addOddFromApiResponse: Error while trying to save Odd List!");
            } else {
                logger.info("addOddFromApiResponse: Saving odd list for Odd Group " + oddGroup.getName());
            }

        }
    }

    @Transactional
    private Fixture createFixtureFromJsonElement(JsonElement jsonElement) {
        try {
            if (!JsonValidation.validateJsonElementFieldIsObject(jsonElement, "fixture") || !JsonValidation.validateJsonElementFieldIsObject(jsonElement, "goals")) {
                return null;
            }

            JsonElement fixtureElement = jsonElement.getAsJsonObject().get("fixture");

            if (!JsonValidation.validateJsonElementFieldIsNumber(fixtureElement, "id") || !JsonValidation.validateJsonElementFieldIsString(fixtureElement, "date") || !JsonValidation.validateJsonElementFieldIsObject(fixtureElement, "status") || !JsonValidation.validateJsonElementFieldIsString(fixtureElement.getAsJsonObject().get("status"), "short")) {
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

    private String oddsApiCall(int leagueId, String date) throws IOException, InterruptedException {
        logger.info("FootballAPI Odds call for getting odds for league");
        String uriString = "https://api-football-v1.p.rapidapi.com/v3/odds?league=" + leagueId + "&season=" + Constants.SEASON + "&date=" + date;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uriString)).header("X-RapidAPI-Key", SecretKeys.getApi_key()).header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com").method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful FootballAPI Odds call for getting odds for league");
        return response.body();
    }

    private String fixturesApiCall(int leagueId, String dateFrom, String dateTo) throws IOException, InterruptedException {
        logger.info("FootballAPI Fixtures call for getting fixtures");
        String uriString = "https://api-football-v1.p.rapidapi.com/v3/fixtures?league=" + leagueId + "&season=" + Constants.SEASON + "&from=" + dateFrom + "&to=" + dateTo + "&timezone=Europe%2FBelgrade";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uriString)).header("X-RapidAPI-Key", SecretKeys.getApi_key()).header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com").method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful Fixtures call for getting fixtures");
        return response.body();
    }

    private String oddGroupsApiResponse() throws IOException, InterruptedException {
        logger.info("Creating Football API call for getting Odd Groups.");
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api-football-v1.p.rapidapi.com/v3/odds/bets")).header("X-RapidAPI-Key", SecretKeys.getApi_key()).header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com").method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful Football API call for getting Odd Groups");
        return response.body();
    }

    private JsonArray getBetsArrayFromBookmakerJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementFieldIsArray(jsonElement, "bookmakers")) {
            return null;
        }
        JsonArray bookmakersArr = jsonElement.getAsJsonObject().get("bookmakers").getAsJsonArray();

        for (JsonElement bookmaker : bookmakersArr) {
            if (!JsonValidation.validateJsonElementFieldIsNumber(bookmaker, "id") || !JsonValidation.validateJsonElementFieldIsArray(bookmaker, "bets")) {
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

    private JsonArray getResponseArrayFromJson(String responseBody) {
        JsonElement responseEl = gsonBuilder.fromJson(responseBody, JsonElement.class);
        if (!JsonValidation.validateJsonElementFieldIsArray(responseEl, "response")) {
            logger.info("Error getting response body\n" + responseBody);
            return null;
        }
        return responseEl.getAsJsonObject().getAsJsonArray("response");
    }

    private OddGroup createOddGroupFromJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementFieldIsNumber(jsonElement, "id") || !JsonValidation.validateJsonElementFieldIsString(jsonElement, "name")) {
            return null;
        }
        int oddGroupID = jsonElement.getAsJsonObject().get("id").getAsInt();
        String oddGroupName = jsonElement.getAsJsonObject().get("name").getAsString();
        return new OddGroup(oddGroupID, oddGroupName, new ArrayList<>());
    }

    private int getTeamGoalsFromJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementIsNumber(jsonElement)) {
            logger.warn("Goal value is invalid [" + jsonElement + "]!");
            return 0;
        }
        return jsonElement.getAsInt();
    }

    private Team createTeamFromJsonElement(JsonElement jsonElement) {
        JsonObject responseObject = jsonElement.getAsJsonObject();
        if (!responseObject.has("id") || !responseObject.has("name")) {
            return null;
        }

        JsonElement idElement = responseObject.get("id");
        JsonElement nameElement = responseObject.get("name");
        if (!idElement.isJsonPrimitive() || !idElement.getAsJsonPrimitive().isNumber() || !nameElement.isJsonPrimitive() || !nameElement.getAsJsonPrimitive().isString()) {
            return null;
        }
        Team team = new Team();
        team.setId(idElement.getAsInt());
        team.setName(nameElement.getAsString());
        return team;
    }

    private Odd crateOddFromJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementFieldIsString(jsonElement, "odd") || !JsonValidation.validateJsonElementFieldIsString(jsonElement, "value")) {
            return null;
        }

        String oddValueString = jsonElement.getAsJsonObject().get("odd").getAsString();
        BigDecimal oddValue = BigDecimal.valueOf(Double.parseDouble(oddValueString));
        String oddName = jsonElement.getAsJsonObject().get("value").getAsString();

        Odd odd = new Odd();
        odd.setOdd(oddValue);
        odd.setName(oddName);
        return odd;
    }

    private String currentDateAddOffsetInFormat(long days) {
        return Utility.formatDate(LocalDateTime.now().plusDays(days));
    }

    private LocalDateTime currentDateAddOffset(long days) {
        return LocalDateTime.now().plusDays(days);
    }

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


