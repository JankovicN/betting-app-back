package rs.ac.bg.fon.controller;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.bg.fon.entity.BetGroup;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.service.FixtureService;
import rs.ac.bg.fon.service.OddService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/odd")
public class OddController {

}
