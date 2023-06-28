package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.entity.Odd;

import java.util.List;

public interface OddRepository  extends JpaRepository<Odd, Integer> {
    List<Odd> findByBetGroupIdAndFixtureState(Integer id, String state);

    List<Odd> findByFixtureStateAndFixtureId(String ns, int fixture);

    List<Odd> findByFixtureIdAndBetGroupId(Integer fixtureId, Integer betGroupId);
}
