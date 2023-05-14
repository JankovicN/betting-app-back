package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.service.FixtureService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/fixture")
public class FixtureController {


    private final FixtureService fixtureService;

    @GetMapping("/ns/{league}")
    public ResponseEntity<List<Fixture>> getAllFixtures(@PathVariable int league) {
        List<Fixture> fixtures = fixtureService.getNotStartedByLeague(league);
        return ResponseEntity.ok().body(fixtures);
    }


    @GetMapping("/get")
    public ResponseEntity<List<Fixture>> getFixtures() {

        List<Fixture> fixtures = fixtureService.getNotStarted();
        return ResponseEntity.ok().body(fixtures);
    }
}
