package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.entity.Odd;

import java.util.List;

@Repository
public interface OddRepository  extends JpaRepository<Odd, Integer> {
    List<Odd> findByBetGroupIdAndFixtureState(Integer id, String state);

    List<Odd> findByFixtureStateAndFixtureId(String ns, Integer fixture);

    List<Odd> findByFixtureStateAndFixtureIdAndBetGroupId(String ns, Integer fixtureId, Integer betGroupId);

    boolean existsByFixtureIdAndBetGroupId(Integer fixtureId, Integer betGroupId);
}
