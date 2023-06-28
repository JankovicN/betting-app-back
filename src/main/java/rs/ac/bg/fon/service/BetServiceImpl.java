package rs.ac.bg.fon.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Bet;
import rs.ac.bg.fon.entity.Odd;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.repository.BetRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BetServiceImpl implements BetService{

    private BetRepository betRepository;
    private OddService oddService;

    @Autowired
    public void setBetRepository(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    @Override
    public Bet save(Bet bet) {
        return betRepository.save(bet);
    }

    @Override
    public void upadateAllBets() {
        betRepository.updateAllBets();
    }

    @Override
    public List<Bet> getBetsFromTicket(String ticketJson, Ticket ticket) throws Exception {
        List<Bet> betList = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement responseEl = gson.fromJson(ticketJson, JsonElement.class);

        JsonArray betsArr = responseEl.getAsJsonObject().get("ticket").getAsJsonObject().getAsJsonArray("bets");
        for (JsonElement jsonElement : betsArr) {
            int oddID = jsonElement.getAsJsonObject().get("bet").getAsJsonObject().get("id").getAsInt();
            Optional<Odd> odd = oddService.getOddById(oddID);
            if (!odd.isPresent()) {
                throw new Exception("Error getting odds!");
            }
            if (!odd.get().getFixture().getState().equals("NS")) {
                throw new Exception("Some game has started, create a new ticket!");
            }
            Bet bet = new Bet();
            bet.setOdd(odd.get());
            bet.setTicket(ticket);
            betList.add(bet);
        }
        return betList;
    }
}
