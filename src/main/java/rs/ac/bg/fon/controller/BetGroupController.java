package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.service.BetGroupService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/betgroup")
public class BetGroupController {

    private final BetGroupService betGroupService;

    @GetMapping("/get")
    public ResponseEntity<List<BetGroup>> getBetGroups() {
        return ResponseEntity.ok().body(betGroupService.getAllBetGroups());
    }
    @GetMapping("/get/{fixture}")
    public ResponseEntity<List<BetGroup>> getBetGroups(@PathVariable int fixture) {
        return ResponseEntity.ok().body(betGroupService.getBetGroupsByFixture(fixture));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBetGroup(@PathVariable String id){
        int groupId=Integer.parseInt(id);
        betGroupService.deleteBetGroup(groupId);
        return ResponseEntity.ok().build();
    }
}
