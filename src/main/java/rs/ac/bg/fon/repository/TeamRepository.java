package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
}
