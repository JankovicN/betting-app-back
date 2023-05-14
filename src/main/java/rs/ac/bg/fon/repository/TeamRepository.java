package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {
}
