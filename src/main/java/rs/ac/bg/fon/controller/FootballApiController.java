package rs.ac.bg.fon.controller;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.service.FixtureService;
import rs.ac.bg.fon.service.FootballApiService;
import rs.ac.bg.fon.service.OddService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/")
public class FootballApiController {

    private final FootballApiService footballApiService;
    private final FixtureService fixtureService;
    private final OddService oddService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/apiBetGroups")
    public ResponseEntity<?> getBetGroupsFromAPI(Authentication auth) {
        return footballApiService.getBetGroupsFromAPI();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/apiOdds")
    public ResponseEntity<?> getOddsFromAPI(Authentication auth) {
        ArrayList<Fixture> fixtures = (ArrayList<Fixture>) fixtureService.getNotStarted();
        for (Fixture fix : fixtures) {
            try (FileReader in = new FileReader("Odds.json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject responseEl = gson.fromJson(in, JsonObject.class);
                JsonArray arr = responseEl.getAsJsonObject().getAsJsonArray("bets");

                for (JsonElement betGroup : arr) {
                    int groupId = betGroup.getAsJsonObject().get("id").getAsInt();
                    String name = betGroup.getAsJsonObject().get("name").getAsString();
                    BetGroup currentGroup = new BetGroup();
                    currentGroup.setId(groupId);
                    currentGroup.setName(name);
                    JsonArray oddValues = betGroup.getAsJsonObject().getAsJsonArray("odds");
                    for (JsonElement oddValue : oddValues) {
                        Odd odd = new Odd();
                        odd.setFixture(fix);
                        odd.setBetGroup(currentGroup);
                        currentGroup.getOdds().add(odd);
                        odd.setName(oddValue.getAsJsonObject().get("value").getAsString());
                        String oddString = oddValue.getAsJsonObject().get("odd").getAsString();
                        BigDecimal oddDecimal = new BigDecimal(oddString).setScale(2, RoundingMode.CEILING);
                        odd.setOdd(oddDecimal);
                        oddService.save(odd);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n" + e);
            }
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/apiFixtures")
    public ResponseEntity<?> getFixturesFromAPI(Authentication auth) {
        return footballApiService.getFixturesFromAPI();
    }

    @PostMapping(path = "/matches")
    public ResponseEntity<?> getNewFixtures(@RequestBody String json) {
        footballApiService.getFixturesFromAPI();
        footballApiService.getOddsFromAPI();
        return ResponseEntity.ok().build();
    }
}
