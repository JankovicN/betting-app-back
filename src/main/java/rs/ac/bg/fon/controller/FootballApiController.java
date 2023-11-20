package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.service.FootballApiService;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
@RequestMapping("api")
public class FootballApiController {

    private final FootballApiService footballApiService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/get/betGroups")
    public CompletableFuture<ResponseEntity<?>> getBetGroupsFromAPI(Authentication auth) {
        CompletableFuture<ApiResponse<?>> result = footballApiService.getBetGroupsFromAPI();

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
    @PostMapping(path = "/new")
    public CompletableFuture<ResponseEntity<?>> getFixturesAndOdds(Authentication auth) {
        CompletableFuture<ApiResponse<?>> fixturesResult = footballApiService.getFixturesFromAPI();

        // Use thenCompose to wait for the completion of the fixturesResult before starting the oddsResult
        CompletableFuture<ApiResponse<?>> combinedResult =
                fixturesResult.thenCompose(fixturesApiResponse -> {
                    CompletableFuture<ApiResponse<?>> oddsResult = footballApiService.getOddsFromAPI();

                    // Merge the data from both ApiResponse objects
                    return oddsResult.thenApply(oddsApiResponse -> {
                        System.out.println(oddsApiResponse.getInfoMessages());
                        System.out.println(fixturesApiResponse.getInfoMessages());
                        fixturesApiResponse.getInfoMessages().addAll(oddsApiResponse.getInfoMessages());
                        fixturesApiResponse.getErrorMessages().addAll(oddsApiResponse.getErrorMessages());
                        return fixturesApiResponse;
                    });
                });

        // Return a new CompletableFuture with additional processing using ApiResponseUtil
        return combinedResult.thenApply(apiResult -> ApiResponseUtil.handleApiResponse(apiResult));

    }
}
