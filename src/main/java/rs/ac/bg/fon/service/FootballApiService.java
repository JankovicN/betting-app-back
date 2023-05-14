package rs.ac.bg.fon.service;

import org.springframework.http.ResponseEntity;

public interface FootballApiService {
    ResponseEntity<?> getBetGroupsFromAPI();
    ResponseEntity<?>  getOddsFromAPI();
    ResponseEntity<?> getFixturesFromAPI();
}
