package rs.ac.bg.fon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Page<User> findByUsernameContaining(String filterUsername, Pageable pageable);
}
