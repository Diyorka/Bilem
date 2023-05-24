package kg.bilem.repository;

import kg.bilem.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByUserId(Long id);

    RefreshToken findByUserId(Long id);

    boolean existsByToken(String token);

    RefreshToken findByToken(String token);
}
