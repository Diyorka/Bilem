package kg.bilem.service.impls;

import kg.bilem.dto.notification.ResponseNotificationDTO;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.enums.Status;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Notification;
import kg.bilem.model.User;
import kg.bilem.repository.NotificationRepository;
import kg.bilem.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static kg.bilem.dto.notification.ResponseNotificationDTO.toResponseNotificationDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    NotificationRepository notificationRepository;

    @Override
    public List<ResponseNotificationDTO> getAllNotificationsByUser(User user) {
        return toResponseNotificationDTO(findNotificationsByUser(user));
    }

    @Override
    public ResponseNotificationDTO getNotificationById(Long id, User user) {
        return toResponseNotificationDTO(notificationRepository.findById(id)
                .filter(notification -> notification.getStatus() == Status.ACTIVE)
                .filter(notification -> notification.getUser().equals(user))
                .orElseThrow(() -> new NotFoundException("Уведомление не найдено"))
        );
    }

    @Override
    public List<ResponseNotificationDTO> markAllNotificationsAsReadByUser(User user) {
        List<Notification> notifications = notificationRepository.findAllByUser(user)
                .stream()
                .filter(notification -> notification.getStatus() == Status.ACTIVE)
                .filter(notification -> !notification.isRead())
                .toList();

        for(Notification notification:notifications){
            notification.setRead(true);
            notificationRepository.save(notification);
        }

        return toResponseNotificationDTO(notifications);
    }

    @Override
    public ResponseNotificationDTO markNotificationAsReadById(Long id, User user) {
        Notification notification = findNotificationByIdAndUser(id, user);

        notification.setRead(true);
        notificationRepository.save(notification);

        return toResponseNotificationDTO(notification);
    }

    @Override
    public ResponseEntity<ResponseWithMessage> deleteNotificationById(Long id, User user) {
        Notification notification = findNotificationByIdAndUser(id, user);

        notification.setStatus(Status.DELETED);
        notificationRepository.save(notification);

        return ResponseEntity.ok(new ResponseWithMessage("Уведомление удалено"));
    }

    @Override
    public ResponseEntity<ResponseWithMessage> deleteAllNotificationsOfUser(User user) {
        List<Notification> notifications = findNotificationsByUser(user);

        for(Notification notification:notifications){
            notification.setStatus(Status.DELETED);
            notificationRepository.save(notification);
        }

        return ResponseEntity.ok(new ResponseWithMessage("Уведомления удалены"));
    }

    private List<Notification> findNotificationsByUser(User user) {
        return notificationRepository.findAllByUser(user)
                .stream()
                .filter(notification -> notification.getStatus() == Status.ACTIVE)
                .toList();
    }

    private Notification findNotificationByIdAndUser(Long id, User user) {
        return notificationRepository.findById(id)
                .filter(n -> n.getUser().equals(user))
                .orElseThrow(
                        () -> new NotFoundException("Уведомление с айди " + id + " не найдено")
                );
    }
}
