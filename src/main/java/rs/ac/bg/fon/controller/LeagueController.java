package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rs.ac.bg.fon.service.LeagueService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequiredArgsConstructor
@RequestMapping("league")
public class LeagueController {

    private final LeagueService leagueService;

    @GetMapping("/get/fixtures")
    public ResponseEntity<?> getAllLeaguesWithFixtures() {
        return ApiResponseUtil.handleApiResponse(leagueService.getAllLeaguesWithFixturesApiResponse());
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllLeagues() {
        return ApiResponseUtil.handleApiResponse(leagueService.getAllLeaguesApiResponse());
    }

    @GetMapping("/ns")
    public ResponseEntity<?> getFixturesForLeague(@RequestParam Integer leagueID) {
        if (leagueID == null) {
            return ApiResponseUtil.errorApiResponse("League data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(leagueService.getNotStartedByLeagueApiCall(leagueID));
    }

}
