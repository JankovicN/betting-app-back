package rs.ac.bg.fon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.service.LeagueService;

import java.io.FileReader;

@Controller
@RequestMapping("api/league")
public class LeagueController {

    private LeagueService leagueService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/leagues")
    public ResponseEntity<?> getLeaguesFromAPI(Authentication auth){
        String userEmail = auth.getPrincipal().toString();
        try ( FileReader in = new FileReader("object.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement responseEl=gson.fromJson(in, JsonElement.class);
            JsonArray arr = responseEl.getAsJsonObject().getAsJsonArray("response");
            for (JsonElement jsonElement : arr) {
                System.out.println(jsonElement);
                int leagueID=jsonElement.getAsJsonObject().get("league").getAsJsonObject().get("id").getAsInt();
                String leagueName=jsonElement.getAsJsonObject().get("league").getAsJsonObject().get("name").getAsString();
                System.out.println("League: "+leagueName+" \nLeague id: "+leagueID);
            }} catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

}
