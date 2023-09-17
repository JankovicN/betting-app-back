package rs.ac.bg.fon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.entity.Payment;

@Repository
public interface PaymentRepository  extends JpaRepository<Payment, Integer> {

    @Query("SELECT SUM(amount) FROM Payment WHERE user_id = :userId")
    Double getUserPayments(@Param("userId") int userId);

}
