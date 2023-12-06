package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.service.FootballApiService;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class FootballApiController {

    private final FootballApiService footballApiService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/get/oddGroups")
    public CompletableFuture<ResponseEntity<?>> getOddGroupsFromAPI(Authentication auth) {
        CompletableFuture<ApiResponse<?>> result = footballApiService.getOddGroupsFromAPI();

        return result.thenApply(apiResult -> ApiResponseUtil.handleApiResponse(apiResult));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/new/fixtures")
    public CompletableFuture<ResponseEntity<?>> getFixturesFromAPI(Authentication auth) {
        CompletableFuture<ApiResponse<?>> result = footballApiService.getFixturesFromAPI();
        return result.thenApply(apiResult -> ApiResponseUtil.handleApiResponse(apiResult));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/new/odds")
    public CompletableFuture<ResponseEntity<?>> getNewOdds(Authentication auth) {
        CompletableFuture<ApiResponse<?>> result = footballApiService.getOddsFromAPI();
        return result.thenApply(apiResult -> ApiResponseUtil.handleApiResponse(apiResult));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/new/fixturesAndOdds")
    public CompletableFuture<ResponseEntity<?>> getFixturesAndOdds(Authentication auth) {
        CompletableFuture<ApiResponse<?>> result = footballApiService.getFixturesAndOddsFromAPI();
        return result.thenApply(apiResult -> ApiResponseUtil.handleApiResponse(apiResult));

    }

    @GetMapping("/exists")
    public ResponseEntity<?> getEexists(@RequestParam Integer fixtureID, @RequestParam Integer oddGroupID) {
        return ApiResponseUtil.handleApiResponse(footballApiService.exists(fixtureID,oddGroupID));
    }
}
