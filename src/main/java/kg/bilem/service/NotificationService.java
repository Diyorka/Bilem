package kg.bilem.service;

import kg.bilem.dto.notification.ResponseNotificationDTO;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NotificationService {
    List<ResponseNotificationDTO> getAllNotificationsByUser(User user);
    ResponseNotificationDTO getNotificationById(Long id, User user);
    List<ResponseNotificationDTO> markAllNotificationsAsReadByUser(User user);
    ResponseNotificationDTO markNotificationAsReadById(Long id, User user);
    ResponseEntity<ResponseWithMessage> deleteNotificationById(Long id, User user);
    ResponseEntity<ResponseWithMessage> deleteAllNotificationsOfUser(User user);
}
