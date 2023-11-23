package rs.ac.bg.fon.service;

import rs.ac.bg.fon.utility.ApiResponse;

import java.util.concurrent.CompletableFuture;

public interface FootballApiService {
    CompletableFuture<ApiResponse<?>> getOddGroupsFromAPI();

    CompletableFuture<ApiResponse<?>> getOddsFromAPI();

    CompletableFuture<ApiResponse<?>> getFixturesFromAPI();

    CompletableFuture<ApiResponse<?>> getFixturesAndOddsFromAPI();


}
