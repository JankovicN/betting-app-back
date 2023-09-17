package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.entity.BetGroup;

import java.util.List;

@Repository
public interface BetGroupRepository  extends JpaRepository<BetGroup, Integer> {

    List<BetGroup> findByOddsFixtureId(int fixture);
}
