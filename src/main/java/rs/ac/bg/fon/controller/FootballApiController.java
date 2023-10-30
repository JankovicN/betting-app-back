package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.service.FootballApiService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/")
public class FootballApiController {

    private final FootballApiService footballApiService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/betGroups")
    public ResponseEntity<?> getBetGroupsFromAPI(Authentication auth) {
        return ApiResponseUtil.handleApiResponse(footballApiService.getBetGroupsFromAPI());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new/fixtures")
    public ResponseEntity<?> getFixturesFromAPI(Authentication auth) {
        return ApiResponseUtil.handleApiResponse(footballApiService.getFixturesFromAPI());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/new/odds")
    public ResponseEntity<?> getNewOdds() {
        return ApiResponseUtil.handleApiResponse(footballApiService.getOddsFromAPI());
    }
}
