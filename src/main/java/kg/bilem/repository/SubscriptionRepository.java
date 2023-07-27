package kg.bilem.repository;

import kg.bilem.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByUserIdAndSubscriberId(Long userId, Long subscriberId);

    Page<Subscription> findAllBySubscriberId(Long id, Pageable pageable);

    Optional<Subscription> findByUserIdAndSubscriberId(Long userId, Long id);

    Page<Subscription> findAllByUserId(Long id, Pageable pageable);
}
