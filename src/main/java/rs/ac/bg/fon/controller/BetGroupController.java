package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.service.BetGroupService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequiredArgsConstructor
@RequestMapping("betGroup")
public class BetGroupController {

    private final BetGroupService betGroupService;

    @GetMapping("/get")
    public ResponseEntity<?> getBetGroups(@RequestParam Integer fixtureID) {
        if (fixtureID == null) {
            return ApiResponseUtil.errorApiResponse("Fixture data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(betGroupService.getBetGroupsByFixtureApiResponse(fixtureID));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBetGroup(@RequestParam Integer betGroupID){
        if (betGroupID == null) {
            return ApiResponseUtil.errorApiResponse("Bet group data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(betGroupService.deleteBetGroupApiResponse(betGroupID));
    }
}
