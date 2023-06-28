package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Odd;

import java.util.List;
import java.util.Optional;

@Service
public interface OddService {

    public Odd save(Odd odd);
    List<Odd> saveOddList(List<Odd> odds);
    List<Odd> getALlOdds();

    List<Odd> getOddsForFixture(int fixture);

    List<Odd> getOddsForFixtureAndBetGroup(Integer fixtureId, Integer betGroupId);

    Optional<Odd> getOddById(int oddID);
}
