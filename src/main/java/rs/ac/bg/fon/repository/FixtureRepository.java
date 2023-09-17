package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.entity.Fixture;

import java.util.Date;
import java.util.List;

@Repository
public interface FixtureRepository  extends JpaRepository<Fixture, Integer> {

    List<Fixture> findByState (String state);

    List<Fixture> findByStateAndLeagueId(String ns, int league);

    List<Fixture> findByDate(Date date);

}
