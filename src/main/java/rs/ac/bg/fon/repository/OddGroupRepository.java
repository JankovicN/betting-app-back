package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.entity.OddGroup;

import java.util.List;

@Repository
public interface OddGroupRepository extends JpaRepository<OddGroup, Integer> {

    List<OddGroup> findByOddsFixtureIdOrderByIdAsc(int fixture);
}
