package kg.bilem.repository;

import kg.bilem.model.RecoveryToken;
import kg.bilem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, Long> {
    boolean existsByToken(String token);

    Optional<RecoveryToken> findByToken(String token);

    boolean existsByUser(User user);

    RecoveryToken findByUser(User user);
}
