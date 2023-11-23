package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rs.ac.bg.fon.service.OddGroupService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequiredArgsConstructor
@RequestMapping("oddGroup")
public class OddGroupController {

    private final OddGroupService oddGroupService;

    @GetMapping("/get")
    public ResponseEntity<?> getOddGroups(@RequestParam Integer fixtureID) {
        if (fixtureID == null) {
            return ApiResponseUtil.errorApiResponse("Fixture data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(oddGroupService.getOddGroupsByFixtureApiResponse(fixtureID));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOddGroup(@RequestParam Integer oddGroupID) {
        if (oddGroupID == null) {
            return ApiResponseUtil.errorApiResponse("Odd Group data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(oddGroupService.deleteOddGroupApiResponse(oddGroupID));
    }
}
