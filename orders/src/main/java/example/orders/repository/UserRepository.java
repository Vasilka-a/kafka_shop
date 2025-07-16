package example.orders.repository;

import example.orders.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select user from User user where user.userId = :userId")
    Optional<User> findUserByUserId(Long userId);
}
