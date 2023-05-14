package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    boolean existsByEmail(String email);

}
