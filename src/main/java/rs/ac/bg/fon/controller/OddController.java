package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.service.FixtureService;
import rs.ac.bg.fon.service.OddService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/odd")
public class OddController {
    private final OddService oddService;
    private final FixtureService fixtureService;

    @GetMapping("/get/odds")
    public ResponseEntity<?> getOddsForFixtures(){
        return ApiResponseUtil.handleApiResponse(oddService.getALlOddsApiResponse());
    }

    @GetMapping("/get/odds/{fixture}")
    public ResponseEntity<?> getOddsForFixture(@PathVariable Integer fixture){
        if (fixture == null) {
            // Handle the case where the "id" path variable is missing
            return ResponseEntity.badRequest().body("ID is missing");
        }
        return ApiResponseUtil.handleApiResponse(oddService.getOddsForFixtureApiResponse(Integer.valueOf(fixture)));
    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/set/odds")
//    public ResponseEntity<?> getOddsFromAPI(Authentication auth) {
//        ArrayList<Fixture> fixtures = (ArrayList<Fixture>) fixtureService.getNotStarted();
//        for (Fixture fix : fixtures) {
//            try (FileReader in = new FileReader("Odds.json")) {
//                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                JsonObject responseEl = gson.fromJson(in, JsonObject.class);
//                System.out.println(responseEl);
//                JsonArray arr = responseEl.getAsJsonObject().getAsJsonArray("bets");
//                // ODD - odd_id(auto), name , odd(kvota), betGroupid, fixtureid
//
//                for (JsonElement getGroup : arr) {
//                    int groupId = getGroup.getAsJsonObject().get("id").getAsInt();
//                    String name = getGroup.getAsJsonObject().get("name").getAsString();
//                    BetGroup currentGroup = new BetGroup();
//                    currentGroup.setId(groupId);
//                    currentGroup.setName(name);
//                    JsonArray oddValues = getGroup.getAsJsonObject().getAsJsonArray("odds");
//                    for (JsonElement oddValue : oddValues) {
//                        Odd odd = new Odd();
//                        odd.setFixture(fix);
//                        fix.getOdds().add(odd);
//                        odd.setBetGroup(currentGroup);
//                        currentGroup.getOdds().add(odd);
//                        odd.setName(oddValue.getAsJsonObject().get("value").getAsString());
//                        String oddString = oddValue.getAsJsonObject().get("odd").getAsString();
//                        BigDecimal oddDecimal = new BigDecimal(oddString).setScale(2, RoundingMode.CEILING);
//                        odd.setOdd(oddDecimal);
//                        oddService.save(odd);
//                    }
//                }
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (Exception e) {
//                System.out.println(e.getMessage() + "\n" + e);
//            }
//        }
//        return ResponseEntity.ok().build();
//    }
}
