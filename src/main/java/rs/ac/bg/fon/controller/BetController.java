package rs.ac.bg.fon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.service.BetService;
import rs.ac.bg.fon.utility.ApiResponseUtil;

@RestController
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
