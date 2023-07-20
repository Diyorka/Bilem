package kg.bilem.repository;

import kg.bilem.model.Notification;
import kg.bilem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);

    Notification findByIdAndUser(Long id, User user);
}
