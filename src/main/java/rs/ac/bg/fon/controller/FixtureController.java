package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.service.FixtureService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("fixture")
public class FixtureController {


    private final FixtureService fixtureService;

    @GetMapping("/ns")
    public ResponseEntity<?> getFixturesForLeague(@RequestParam Integer leagueID) {
        if(leagueID == null){
            return ApiResponseUtil.errorApiResponse("League data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(fixtureService.getNotStartedByLeagueApiCall(leagueID));
    }

    @GetMapping("/ns/leagues")
    public ResponseEntity<?> getFixturesForLeagues(@RequestBody List<Integer> leagues) {
        if(leagues == null || leagues.isEmpty()){
            return ApiResponseUtil.errorApiResponse("League data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(fixtureService.getNotStartedByLeaguesApiCall(leagues));
    }
}
