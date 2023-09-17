package rs.ac.bg.fon.service;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.constants.Constants;
import rs.ac.bg.fon.constants.SecretKeys;
import rs.ac.bg.fon.entity.*;
import rs.ac.bg.fon.utility.ApiResponse;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
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

    private final SimpleDateFormat dateTimeFormetter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime[] getDateRange(LocalDateTime start, LocalDateTime end) {
        long numDays = ChronoUnit.DAYS.between(start, end);
        LocalDateTime[] dates = new LocalDateTime[(int) numDays];
        for (int i = 0; i < numDays; i++) {
            dates[i] = start.plusDays(i);
        }
        return dates;
    }

    @Override
    public ApiResponse<?> getBetGroupsFromAPI() {
        ApiResponse apiResponse = new ApiResponse<>();
        if (betGroupService.countRows() == 0) {
            try {
                String responseBody = betGroupsApiCall();
                JsonArray arr = getResponseArrayFromJson(responseBody);
                if (arr == null || arr.isJsonNull() || arr.isEmpty()) {
                    logger.info("FootballAPI BetGroups call returned empty array");
                    apiResponse.addInfoMessage("No new bet groups were added!");
                    return apiResponse;
                }
                for (JsonElement jsonElement : arr) {
                    BetGroup group = createBetGroupFromJsonElement(jsonElement);
                    betGroupService.saveBetGroup(group);
                }
                apiResponse.addInfoMessage("Successfully added new bet groups!");
                logger.info("Successful FootballAPI BetGroups call for getting bet groups");
            } catch (IOException e) {
                apiResponse.addErrorMessage("Error getting bet groups from Football API [IOException]");
                logger.error("IOException: FootballAPI BetGroups Error transforming response to bet groups");
            } catch (Exception e) {
                apiResponse.addErrorMessage("Error getting bet groups from Football API [Exception]");
                logger.error("Exception: FootballAPI BetGroups Error when getting bet groups", e);
            }
        }else{
            logger.info("Bet groups are already fetched!");
            apiResponse.addInfoMessage("No new bet groups were added!");
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<?> getOddsFromAPI() {
        LocalDateTime[] dateRange = getDateRange(currentDateAddOffset(0), currentDateAddOffset(5));
        ApiResponse apiResponse = new ApiResponse<>();
        for (LocalDateTime localDateTime : dateRange) {
            for (League league : leagueService.getAllLeagues()) {
                try {
                    String responseBody = oddsApiCall(league.getId(), dateFormatter.format(localDateTime));
                    JsonArray jsonArr = getResponseArrayFromJson(responseBody);
                    if (jsonArr == null || jsonArr.isJsonNull() || jsonArr.isEmpty()) {
                        continue;
                    }
                    for (JsonElement jsonElement : jsonArr) {
                        Fixture fixture = fixtureService.getFixtureById(jsonElement.getAsJsonObject().get("fixture").getAsJsonObject().get("id").getAsInt());
                        if (fixture.getState().equals("NS")) {
                            JsonArray betsArray = getBetsArrayFromBookmakerJsonElement(jsonElement);
                            if (betsArray == null || betsArray.isJsonNull() || betsArray.isEmpty()) {
                                continue;
                            }
                            for (JsonElement betGroupEl : betsArray) {
                                Integer betGroupId = Integer.valueOf(betGroupEl.getAsJsonObject().get("id").getAsInt());
                                if (betGroupService.existsWithId(betGroupId)) {
                                    if (oddService.existsWithFixtureIdAndBetGroupId(betGroupId, fixture.getId())) {
                                        continue;
                                    }
                                    BetGroup betGroup = betGroupService.getBetGroupWithId(betGroupId);
                                    JsonArray oddsArr = betGroupEl.getAsJsonObject().get("values").getAsJsonArray();
                                    List<Odd> oddList = new ArrayList<>();
                                    if (oddsArr == null || oddsArr.isJsonNull() || oddsArr.isEmpty()) {
                                        continue;
                                    }
                                    for (JsonElement oddsEl : oddsArr) {
                                        Odd odd = crateOddFromJsonElement(oddsEl);
                                        odd.setFixture(fixture);
                                        odd.setBetGroup(betGroup);
                                        oddList.add(odd);
                                        logger.info("Adding odd to list " + odd.getName() + " for fixture " + fixture.getHome().getName() + " - " + fixture.getAway().getName());
                                    }
                                    betGroup.getOdds().addAll(oddList);
                                    betGroupService.saveBetGroup(betGroup);
                                    oddService.saveOddList(oddList);
                                    logger.info("Saving odd list for bet group " + betGroup.getName());
                                }
                            }
                        }
                    }
                    apiResponse.addInfoMessage("Successfully added new odds!");
                    logger.info("Successful FootballAPI Odds call for getting odds, for date " + localDateTime);
                } catch (IOException e) {
                    apiResponse.addErrorMessage("Error getting odds from Football API [IOException]");
                    logger.error("IOException: FootballAPI Odds Error transforming response to Odd");
                } catch (Exception e) {
                    apiResponse.addErrorMessage("Error getting odds from Football API [Exception]");
                    logger.error("Exception: FootballAPI Odds Error when getting Odds", e);
                }
            }
        }
        return apiResponse;
    }


    @Override
    public ApiResponse<?> getFixturesFromAPI() {
        String dateFromString = currentDateAddOffsetInFormat(-1);
        String dateToString = currentDateAddOffsetInFormat(5);
        ApiResponse apiResponse = new ApiResponse<>();
        for (League league : leagueService.getAllLeagues()) {
            try {
                String responseBody = fixturesApiCall(league.getId(), dateFromString, dateToString);
                JsonArray jsonArr = getResponseArrayFromJson(responseBody);
                if (jsonArr == null || jsonArr.isJsonNull() || jsonArr.isJsonNull() || jsonArr.isEmpty()) {
                    continue;
                }
                for (JsonElement jsonElement : jsonArr) {

                    JsonObject teams = jsonElement.getAsJsonObject().get("teams").getAsJsonObject();
                    Team home = createTeamFromJsonElement(teams.get("home"));
                    Team away = createTeamFromJsonElement(teams.get("away"));
                    teamService.save(home);
                    teamService.save(away);

                    Fixture fixture = createFixtureFromJsonElement(jsonElement);
                    fixture.setOdds(new ArrayList<>());
                    home.getHome().add(fixture);
                    away.getAway().add(fixture);
                    fixture.setHome(home);
                    fixture.setAway(away);
                    league.getFixtures().add(fixture);
                    fixture.setLeague(league);

                    fixtureService.save(fixture);
                }
                apiResponse.addInfoMessage("Successfully added new fixtures!");
                logger.info("Successful FootballAPI Fixtures call for getting fixtures");
            } catch (IOException e) {
                apiResponse.addErrorMessage("Error getting fixtures from Football API [IOException]");
                logger.error("IOException: FootballAPI Fixtures Error transforming response to fixture");
            } catch (ParseException e) {
                apiResponse.addErrorMessage("Error getting fixtures from Football API [ParseException]");
                logger.error("ParseException: FootballAPI Fixtures Error parsing date when getting fixtures");
            } catch (Exception e) {
                apiResponse.addErrorMessage("Error getting fixtures from Football API [Exception]");
                logger.error("Exception: FootballAPI Fixtures Error when getting fixtures", e);
            }
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<?> test() {
//            League testLeague = new League(1, "testLeague", null);
//            Team homeTeam = new Team(1,"homeTeam",null,null);
//            Team awayTeam = new Team(2,"awayTeam",null,null);
//            Fixture testFixture = new Fixture(1,new Date(), homeTeam, awayTeam,1,2,"test", testLeague, null, null);
//            BetGroup testBetGroup = new BetGroup(1, "testBetGroup", null);
//            Odd testOdd = new Odd(1, BigDecimal.valueOf(1.2), "testOdd", testFixture, testBetGroup, null);
//
//            List<Fixture> testFixtureList = new ArrayList<>();
//            testFixtureList.add(testFixture);
//            testLeague.setFixtures(testFixtureList);
//            homeTeam.setHome(testFixtureList);
//            awayTeam.setAway(testFixtureList);
//
//            List<Odd> testOddList = new ArrayList<>();
//            testOddList.add(testOdd);
//            testBetGroup.setOdds(testOddList);
//            testFixture.setOdds(testOddList);
//
//            List<BetGroup> testBetGroupList = new ArrayList<>();
//            testBetGroupList.add(testBetGroup);
//            testFixture.setBetGroupList(testBetGroupList);
//
//        try {
//            TeamDTO homeDto = teamMapper.teamToTeamDTO(homeTeam);
//            TeamDTO awayDto = teamMapper.teamToTeamDTO(awayTeam);
//            List<OddDTO> testOddDtoList = new ArrayList<>();
//            for (Odd odd:testOddList) {
//                OddDTO oddDTO = oddMapper.oddToOddDTO(odd);
//                testOddDtoList.add(oddDTO);
//                testOddDtoList.add(oddDTO);
//            }
//            List<BetGroupDTO> betGroupDTOList = new ArrayList<>();
//            for (BetGroup betGroup:testBetGroupList) {
//                BetGroupDTO betGroupDTO = betGroupMapper.betGroupToBetGroupDTO(betGroup,testOddDtoList);
//                betGroupDTOList.add(betGroupDTO);
//                betGroupDTOList.add(betGroupDTO);
//            }
//            FixtureDTO fixtureDTO = fixtureMapper.fixtureToFixtureDTO(testFixture, homeDto,awayDto,betGroupDTOList );
//            List<FixtureDTO> fixtureDTOS = new ArrayList<>();
//            fixtureDTOS.add(fixtureDTO);
//            fixtureDTOS.add(fixtureDTO);
//            LeagueDTO league = leagueMapper.leagueToLeagueDTO(testLeague, fixtureDTOS);
//
//            List<LeagueDTO> leagueDTOS = new ArrayList<>();
//            leagueDTOS.add(league);
//            leagueDTOS.add(league);
//            return ResponseEntity.ok().body(leagueDTOS);
//        }catch (Exception e){
//            logger.error(e.getMessage());
//        }
//
//        try {
//            BetGroupDTO betGroupDTO = betGroupMapper.betGroupToBetGroupDTO(testBetGroup);
//            logger.info("betGroupDTO");
//        }catch (Exception e){
//            logger.error(e.getMessage());
//        }
//        try {
//            OddDTO oddDTO = oddMapper.oddToOddDTO(testOdd);
//            logger.info("oddDTO");
//        }catch (Exception e){
//            logger.error(e.getMessage());
//        }
//        try {
//            FixtureDTO fixtureDTO = fixtureMapper.fixtureToFixtureDTO(testFixture);
//            logger.info("fixtureDTO");
//        }catch (Exception e){
//            logger.error(e.getMessage());
//        }
//        try {
//            LeagueDTO leagueDTO = leagueMapper.leagueToLeagueDTO(testLeague);
//            logger.info("leagueDTO");
//        }catch (Exception e){
//            logger.error(e.getMessage());
//        }
//        try {
//            TeamDTO awayTeamDTO = teamMapper.teamToTeamDTO(awayTeam);
//            logger.info("awayTeamDTO");
//        }catch (Exception e){
//            logger.error(e.getMessage());
//        }
//        try {
//            TeamDTO homeTeamDTO = teamMapper.teamToTeamDTO(homeTeam);
//            logger.info("homeTeamDTO");
//        }catch (Exception e){
//            logger.error(e.getMessage());
//        }

        return null;
    }


    public Fixture createFixtureFromJsonElement(JsonElement jsonElement) throws ParseException {
        try {
            JsonObject fixtureJson = jsonElement.getAsJsonObject().get("fixture").getAsJsonObject();
            if (fixtureJson != null && !fixtureJson.isJsonNull() && fixtureJson.isJsonObject()) {
                Fixture fixture = new Fixture();
                fixture.setId(fixtureJson.get("id").getAsInt());
                String date = fixtureJson.get("date").getAsString();
                date = date.substring(0, date.indexOf("+"));
                date = date.replace("T", " ");
                Date formattedDate = dateTimeFormetter.parse(date);
                fixture.setDate(formattedDate);

                fixture.setState(fixtureJson.get("status").getAsJsonObject().get("short").getAsString());
                JsonObject goals = jsonElement.getAsJsonObject().get("goals").getAsJsonObject();
                fixture.setHomeGoals(getTeamGoalsFromJsonElement(goals.get("home")));
                fixture.setAwayGoals(getTeamGoalsFromJsonElement(goals.get("away")));
                return fixture;
            }
        } catch (Exception e) {
            logger.error("Exception: FootballAPI Fixtures Error parsing fixture json\n" + e);
        }
        return null;

    }

    public String oddsApiCall(int leagueId, String date) throws IOException, InterruptedException {
        logger.info("FootballAPI Odds call for getting odds for league");
        String uriString = "https://api-football-v1.p.rapidapi.com/v3/odds?league=" + leagueId + "&season=" + Constants.SEASON + "&date=" + date;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uriString)).header("X-RapidAPI-Key", SecretKeys.getApi_key())
                .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com").method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful FootballAPI Odds call for getting odds for league");
        return response.body();
    }

    public String fixturesApiCall(int leagueId, String dateFrom, String dateTo) throws IOException, InterruptedException {
        logger.info("FootballAPI Fixtures call for getting fixtures");
        String uriString = "https://api-football-v1.p.rapidapi.com/v3/fixtures?league=" + leagueId + "&season=" + Constants.SEASON + "&from=" + dateFrom + "&to=" + dateTo + "&timezone=Europe%2FBelgrade";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uriString)).header("X-RapidAPI-Key", SecretKeys.getApi_key())
                .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com").method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful Fixtures call for getting fixtures");
        return response.body();
    }

    public String betGroupsApiCall() throws IOException, InterruptedException {
        logger.info("FootballAPI BetGroups call for getting bet groups");
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api-football-v1.p.rapidapi.com/v3/odds/bets"))
                .header("X-RapidAPI-Key", SecretKeys.getApi_key()).header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Successful FootballAPI BetGroups call for getting bet groups");
        return response.body();
    }

    public JsonArray getBetsArrayFromBookmakerJsonElement(JsonElement jsonElement) {
        JsonArray bookmakersArr = jsonElement.getAsJsonObject().get("bookmakers").getAsJsonArray();
        if (bookmakersArr.isEmpty()) {
            return null;
        }
        JsonElement bet365Bookmaker = null;
        for (JsonElement bookmaker : bookmakersArr) {
            if (bookmaker.getAsJsonObject().get("id").getAsInt() == 8) {
                bet365Bookmaker = bookmaker;
                break;
            }
        }

        return bet365Bookmaker.isJsonNull() ? null : bet365Bookmaker.getAsJsonObject().get("bets").getAsJsonArray();
    }

    public JsonArray getResponseArrayFromJson(String responseBody) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement responseEl = gson.fromJson(responseBody, JsonElement.class);
        return responseEl.getAsJsonObject().getAsJsonArray("response");
    }

    public BetGroup createBetGroupFromJsonElement(JsonElement jsonElement) {
        int betGroupID = jsonElement.getAsJsonObject().get("id").getAsInt();
        String betGroupName = jsonElement.getAsJsonObject().get("name").getAsString();
        return new BetGroup(betGroupID, betGroupName, new ArrayList<>());
    }


    public int getTeamGoalsFromJsonElement(JsonElement jsonElement) {
        if (jsonElement.isJsonNull()) {
            return 0;
        }
        return jsonElement.getAsInt();
    }

    public Team createTeamFromJsonElement(JsonElement jsonElement) {
        Team team = new Team();
        team.setId(jsonElement.getAsJsonObject().get("id").getAsInt());
        team.setName(jsonElement.getAsJsonObject().get("name").getAsString());
        return team;
    }

    private Odd crateOddFromJsonElement(JsonElement jsonElement) {
        Odd odd = new Odd();
        odd.setOdd(jsonElement.getAsJsonObject().get("odd").getAsBigDecimal());
        odd.setName(jsonElement.getAsJsonObject().get("value").getAsString());
        return odd;
    }

    public String currentDateAddOffsetInFormat(long days) {
        return dateFormatter.format(LocalDateTime.now().plusDays(days));
    }

    public LocalDateTime currentDateAddOffset(long days) {
        return LocalDateTime.now().plusDays(days);
    }


}


