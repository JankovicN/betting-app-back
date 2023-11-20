package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.entity.Fixture;

import java.util.List;

@Repository
public interface FixtureRepository  extends JpaRepository<Fixture, Integer> {

    List<Fixture> findByState (String state);

    List<Fixture> findAllByStateAndLeagueIdOrderByDateAsc(String ns, int league);
}
