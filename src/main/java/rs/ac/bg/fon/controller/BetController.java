package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.service.TicketService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@Controller
@RequiredArgsConstructor
@RequestMapping("bet")
public class BetController {

    private final BetService betService;

    @GetMapping("/get")
    public ResponseEntity<?> getBetsForTicket(@RequestParam Integer ticketID) {
        if (ticketID == null || ticketID < 0) {
            return ApiResponseUtil.errorApiResponse("Ticket data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(betService.getBetsForTicketApiResponse(ticketID));
    }
}
