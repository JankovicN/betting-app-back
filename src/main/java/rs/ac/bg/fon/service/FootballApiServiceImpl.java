package rs.ac.bg.fon.service;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.constants.SecretKeys;
import rs.ac.bg.fon.entity.*;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.JsonValidation;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FootballApiServiceImpl implements FootballApiService {
    private static final Logger logger = LoggerFactory.getLogger(FootballApiServiceImpl.class);
    private final LeagueService leagueService;
    private final FixtureService fixtureService;
    private final BetGroupService betGroupService;
    private final TeamService teamService;
    private final OddService oddService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

    private LocalDateTime[] getDateRange(LocalDateTime start, LocalDateTime end) {
        long numDays = ChronoUnit.DAYS.between(start, end);
        LocalDateTime[] dates = new LocalDateTime[(int) numDays];
        for (int i = 0; i < numDays; i++) {
            dates[i] = start.plusDays(i);
        }
        return dates;
    }

    @Transactional
    @Override
    public ApiResponse<?> getBetGroupsFromAPI() {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            if (betGroupService.exists()) {
                try {
                    String responseBody = betGroupsApiResponse();

                    if (responseBody == null || responseBody.isEmpty()) {
                        logger.error("Invalid response from external API!\nFootballAPI BetGroups call returned empty response!");
                        apiResponse.addErrorMessage("Invalid response from external API!");
                        return apiResponse;
                    }

                    JsonArray responseArray = getResponseArrayFromJson(responseBody);

                    if (responseArray == null) {
                        logger.info("No Bet Groups were added!\nFootballAPI BetGroups call returned empty array!");
                        apiResponse.addInfoMessage("No Bet Groups were added!");
                        return apiResponse;
                    }

                    for (JsonElement jsonElement : responseArray) {
                        BetGroup group = createBetGroupFromJsonElement(jsonElement);
                        if (betGroupService.saveBetGroup(group) == null) {
                            logger.warn("Error while saving Bet Group " + group + "!");
                        } else {
                            apiResponse.addInfoMessage("Successfully added new Bet Group " + group + "!");
                        }
                    }
                    logger.info("Successful added Bet Groups!");
                } catch (IOException e) {
                    apiResponse.addErrorMessage("Error getting Bet Groups from Football API!");
                    logger.error("FootballAPI Bet Groups Error transforming response to Bet Group!", e);
                } catch (Exception e) {
                    apiResponse.addErrorMessage("Error getting Bet Groups from Football API!");
                    logger.error("FootballAPI BetGroups Error when getting Bet Groups!", e);
                }
            } else {
                logger.info("Bet groups are already fetched!");
                apiResponse.addInfoMessage("No new Bet Groups were added!");
            }
        } catch (Exception e) {
            apiResponse.addErrorMessage("Error getting Bet Groups from Football API");
            logger.error("FootballAPI Bet Group Error when getting Bet Groups", e);
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<?> getOddsFromAPI() {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            LocalDateTime[] dateRange = getDateRange(currentDateAddOffset(0), currentDateAddOffset(5));
            for (LocalDateTime localDateTime : dateRange) {
                List<League> leagueList = leagueService.getAllLeagues();
                if (leagueList == null) {
                    logger.warn("List of Leagues is empty!");
                    continue;
                }
                for (League league : leagueList) {
                    try {
                        String responseBody = oddsApiCall(league.getId(), dateFormatter.format(localDateTime));
                        if (responseBody == null || responseBody.isEmpty()) {
                            logger.error("Invalid response from external API!\nFootballAPI Odds call returned empty response!\n" + responseBody);
                            apiResponse.addErrorMessage("Invalid response from external API!");
                            continue;
                        }

                        JsonArray jsonArr = getResponseArrayFromJson(responseBody);
                        if (jsonArr == null) {
                            logger.warn("No Odds were added!\nFootballAPI Odds call response field is empty!\n" + jsonArr);
                            apiResponse.addInfoMessage("No Odds were added, for Date = " + dateFormatter.format(localDateTime) + " and League = " + league + "!");
                            continue;
                        }

                        for (JsonElement jsonElement : jsonArr) {
                            addOddFromApiResponse(jsonElement);
                        }
                        apiResponse.addInfoMessage("Successfully added new Odds, for Date = " + dateFormatter.format(localDateTime) + " and League = " + league + "!");
                        logger.info("Successful FootballAPI Odds call for getting Odds, for Date = " + dateFormatter.format(localDateTime) + " and League = " + league + "!");
                    } catch (IOException e) {
                        apiResponse.addErrorMessage("Error getting Odds from Football API , for Date = " + dateFormatter.format(localDateTime) + " and League = " + league + "!");
                        logger.error("IOException: FootballAPI Odds Error transforming response to Odd, for Date = " + dateFormatter.format(localDateTime) + "and League = " + league + "!");
                    } catch (Exception e) {
                        apiResponse.addErrorMessage("Error getting Odds from Football API, for Date = " + dateFormatter.format(localDateTime) + " and League = " + league + "!");
                        logger.error("FootballAPI Odds Error when getting Odds, for Date = " + dateFormatter.format(localDateTime) + " and League = " + league + "!", e);
                    }
                }
            }
        } catch (Exception e) {
            apiResponse.addErrorMessage("Error getting Odds from Football API");
            logger.error("FootballAPI Odds Error when getting all Odds", e);
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<?> getFixturesFromAPI() {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            String dateFromString = currentDateAddOffsetInFormat(-1);
            String dateToString = currentDateAddOffsetInFormat(5);
            List<League> leagueList = leagueService.getAllLeagues();
            if (leagueList == null) {
                logger.warn("List of Leagues is empty!");
                apiResponse.addInfoMessage("No Leagues found!\nContact support for more information!");
                return apiResponse;
            }
            for (League league : leagueList) {
                try {
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
        return apiResponse;
    }

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
        if (fixture == null
                || !fixture.getState().equals(Constants.FIXTURE_NOT_STARTED)) {
            logger.warn("addOddFromApiResponse: Invalid Fixture!\n" + fixture);
            return;
        }

        JsonArray betsArray = getBetsArrayFromBookmakerJsonElement(jsonElement);
        if (betsArray == null) {
            logger.warn("addOddFromApiResponse: Bet Array is null!\n");
            return;
        }
        for (JsonElement betGroupEl : betsArray) {

            // Checking if JSON field are present and of correct type
            if (!JsonValidation.validateJsonElementFieldIsNumber(betGroupEl, "id")
                    || !JsonValidation.validateJsonElementFieldIsArray(betGroupEl, "values")) {
                return;
            }

            // Getting bet group id from JSON
            Integer betGroupId = betGroupEl.getAsJsonObject().get("id").getAsInt();
            // If we don't have that bet group or if odds are already saved fo that fixture and bet group then return
            if (!betGroupService.existsWithId(betGroupId)
                    || oddService.existsWithFixtureIdAndBetGroupId(betGroupId, fixture.getId())) {
                return;
            }

            // Get bet group with ID
            // If betGroup is null it means that an error has occurred, that is why we return
            BetGroup betGroup = betGroupService.getBetGroupWithId(betGroupId);
            if (betGroup == null) {
                return;
            }

            // Create list of odds, go through JSON and create odds from it
            // Then add those odds to list
            // Log if error has occurred while creating odd
            List<Odd> oddList = new ArrayList<>();
            JsonArray oddsArr = betGroupEl.getAsJsonObject().get("values").getAsJsonArray();
            for (JsonElement oddsEl : oddsArr) {
                Odd odd = crateOddFromJsonElement(oddsEl);
                if (odd == null) {
                    logger.warn("addOddFromApiResponse: Error while trying to crate Odd from Json \n" + oddsEl);
                    return;
                }
                odd.setFixture(fixture);
                odd.setBetGroup(betGroup);
                oddList.add(odd);
                logger.info("addOddFromApiResponse: Adding odd to list " + odd.getName() + " for fixture " + fixture.getHome().getName() + " - " + fixture.getAway().getName());
            }

            // Save all odds that we created
            // Log if error has occurred
            if (oddService.saveOddList(oddList) == null) {
                logger.warn("addOddFromApiResponse: Error while trying to save Odd List!");
            } else {
                logger.info("addOddFromApiResponse: Saving odd list for Bet Group " + betGroup.getName());
            }

        }
    }

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
            Date formattedDate = dateTimeFormatter.parse(date);

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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriString))
                .header("X-RapidAPI-Key", SecretKeys.getApi_key())
                .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful FootballAPI Odds call for getting odds for league");
        return response.body();
    }

    private String fixturesApiCall(int leagueId, String dateFrom, String dateTo) throws IOException, InterruptedException {
        logger.info("FootballAPI Fixtures call for getting fixtures");
        String uriString = "https://api-football-v1.p.rapidapi.com/v3/fixtures?league=" + leagueId + "&season=" + Constants.SEASON + "&from=" + dateFrom + "&to=" + dateTo + "&timezone=Europe%2FBelgrade";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriString))
                .header("X-RapidAPI-Key", SecretKeys.getApi_key())
                .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful Fixtures call for getting fixtures");
        return response.body();
    }

    private String betGroupsApiResponse() throws IOException, InterruptedException {
        logger.info("Creating Football API call for getting Bet Groups.");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api-football-v1.p.rapidapi.com/v3/odds/bets"))
                .header("X-RapidAPI-Key", SecretKeys.getApi_key())
                .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful Football API call for getting Bet Groups");
        return response.body();
    }

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

    private JsonArray getResponseArrayFromJson(String responseBody) {
        JsonElement responseEl = gsonBuilder.fromJson(responseBody, JsonElement.class);
        if (!JsonValidation.validateJsonElementFieldIsArray(responseEl, "response")) {
            logger.info("Error getting response body\n" + responseBody);
            return null;
        }
        return responseEl.getAsJsonObject().getAsJsonArray("response");
    }

    private BetGroup createBetGroupFromJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementFieldIsNumber(jsonElement, "id")
                || !JsonValidation.validateJsonElementFieldIsString(jsonElement, "name")) {
            return null;
        }
        int betGroupID = jsonElement.getAsJsonObject().get("id").getAsInt();
        String betGroupName = jsonElement.getAsJsonObject().get("name").getAsString();
        return new BetGroup(betGroupID, betGroupName, new ArrayList<>());
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

    private Odd crateOddFromJsonElement(JsonElement jsonElement) {
        if (!JsonValidation.validateJsonElementFieldIsString(jsonElement, "odd")
                || !JsonValidation.validateJsonElementFieldIsString(jsonElement, "value")) {
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
        return dateFormatter.format(LocalDateTime.now().plusDays(days));
    }

    private LocalDateTime currentDateAddOffset(long days) {
        return LocalDateTime.now().plusDays(days);
    }


}


