package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.service.BetGroupService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/betgroup")
public class BetGroupController {

    private final BetGroupService betGroupService;

    @GetMapping("/get")
    public ResponseEntity<?> getBetGroups() {
        return ApiResponseUtil.handleApiResponse(betGroupService.getAllBetGroupsApiResponse());
    }
    @GetMapping("/get/{fixture}")
    public ResponseEntity<?> getBetGroups(@PathVariable Integer fixtureId) {
        if (fixtureId == null) {
            return ApiResponseUtil.errorApiResponse("Fixture data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(betGroupService.getBetGroupsByFixtureApiResponse(fixtureId));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBetGroup(@PathVariable Integer betGroupId){
        if (betGroupId == null) {
            return ApiResponseUtil.errorApiResponse("Bet group data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(betGroupService.deleteBetGroupApiResponse(betGroupId));
    }
}
