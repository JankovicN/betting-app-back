package rs.ac.bg.fon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.service.LeagueService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequestMapping("api/league")
public class LeagueController {

    private LeagueService leagueService;

    @Autowired
    public void setLeagueService(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping("/get/fixtures")
    public ResponseEntity<?> getAllLeaguesWithFixtures() {
        return ApiResponseUtil.handleApiResponse(leagueService.getAllLeaguesDTOS());
    }

}
