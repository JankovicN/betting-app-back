package rs.ac.bg.fon.service;

import rs.ac.bg.fon.utility.ApiResponse;

public interface FootballApiService {
    ApiResponse<?> getBetGroupsFromAPI();
    ApiResponse<?>  getOddsFromAPI();
    ApiResponse<?> getFixturesFromAPI();
}
