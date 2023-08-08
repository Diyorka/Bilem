package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.notification.ResponseNotificationDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.NotificationServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с уведомлениями",
        description = "В этом контроллере есть возможность получения и удаления модулей"
)
public class NotificationController {
    NotificationServiceImpl notificationServiceImpl;

    @GetMapping("/all")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение всех уведомлений авторизованного пользователя"
    )
    public List<ResponseNotificationDTO> getAllNotificationsByUser(@AuthenticationPrincipal User user){
        return notificationServiceImpl.getAllNotificationsByUser(user);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение уведомления по айди"
    )
    public ResponseNotificationDTO getNotificationById(@PathVariable Long id,
                                                       @AuthenticationPrincipal User user){
        return notificationServiceImpl.getNotificationById(id, user);
    }

    @PutMapping("/all-read")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Отметить все уведомления как прочитанное"
    )
    public List<ResponseNotificationDTO> markAllNotificationsAsReadByUser(@AuthenticationPrincipal User user){
        return notificationServiceImpl.markAllNotificationsAsReadByUser(user);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Отметить уведомление как прочитанное"
    )
    public ResponseNotificationDTO markNotificationAsReadById(@PathVariable Long id,
                                                              @AuthenticationPrincipal User user){
        return notificationServiceImpl.markNotificationAsReadById(id, user);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удалить уведомление по айди"
    )
    public ResponseEntity<String> deleteNotificationById(@PathVariable Long id,
                                                         @AuthenticationPrincipal User user){
        return notificationServiceImpl.deleteNotificationById(id, user);
    }

    @DeleteMapping("/all")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удалить все уведомления авторизованного пользователя"
    )
    public ResponseEntity<String> deleteAllNotificationsOfUser(@AuthenticationPrincipal User user){
        return notificationServiceImpl.deleteAllNotificationsOfUser(user);
    }
}
