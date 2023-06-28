package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.League;
import rs.ac.bg.fon.service.FixtureOddsService;
import rs.ac.bg.fon.utility.ApiResponse;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/fixture_odds")
public class FixtureOddsController {

    private final FixtureOddsService fixtureOddsService;


    @GetMapping("/fixtures")
    public ResponseEntity<ApiResponse<List<League>>> getFixturesBasic() {
        try {
            List<League> allLeagues = fixtureOddsService.getFixturesBasic();
            return ResponseEntity.ok().body(new ApiResponse<>(allLeagues,"",""));
        } catch (Exception e) {
            System.out.println("Error in: getFixturesBasic() \n"+e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null,"","Error while getting fixtures.\nPlease try again later."));
        }
    }

    @GetMapping("/fixtures/filter")
    public ResponseEntity<ApiResponse<List<League>>> getFixturesBasicFilter(@RequestBody List<League> leagues) {
        try {
            List<League> allLeagues=new ArrayList<>();
            for (League league: leagues ){
                allLeagues.add(fixtureOddsService.getFixturesBasicFilter(league));
            }
            return ResponseEntity.ok().body(new ApiResponse<>(allLeagues,"",""));
        } catch (Exception e) {
            System.out.println("Error in: getFixturesBasic() \n"+e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null,"","Error while getting fixtures.\nPlease try again later."));
        }
    }

    @GetMapping("/odds/{fixtureId}")
    public ResponseEntity<ApiResponse<Fixture>> getFixtureWithOdds(@PathVariable int fixtureId) {
        try {
            Fixture fixture = fixtureOddsService.getFixtureWithOdds(fixtureId);
            return ResponseEntity.ok().body(new ApiResponse<>(fixture,"",""));
        } catch (Exception e) {
            System.out.println("Error in: getFixtureWithOdds() \n"+e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null,"","Error while getting fixture odds.\nPlease try again later."));
        }
    }
}
