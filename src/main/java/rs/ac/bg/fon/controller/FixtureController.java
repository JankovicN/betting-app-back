package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.service.FixtureService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/fixture")
public class FixtureController {


    private final FixtureService fixtureService;

    @GetMapping("/ns/{league}")
    public ResponseEntity<?> getAllFixtures(@PathVariable Integer league) {
        if(league == null){
            return ApiResponseUtil.errorApiResponse("League data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(fixtureService.getNotStartedByLeagueApiCall(league));
    }


    @GetMapping("/get")
    public ResponseEntity<?> getFixtures() {
        return ApiResponseUtil.handleApiResponse(fixtureService.getNotStartedApiCall());
    }
}
