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

}
