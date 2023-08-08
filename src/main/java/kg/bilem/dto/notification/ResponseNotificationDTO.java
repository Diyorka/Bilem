package kg.bilem.dto.notification;

import kg.bilem.model.Notification;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseNotificationDTO {
    Long id;

    String header;

    String message;

    boolean read;

    public static ResponseNotificationDTO toResponseNotificationDTO(Notification notification){
        return ResponseNotificationDTO.builder()
                .id(notification.getId())
                .header(notification.getHeader())
                .message(notification.getMessage())
                .read(notification.isRead())
                .build();
    }

    public static List<ResponseNotificationDTO> toResponseNotificationDTO(List<Notification> notifications){
        return notifications.stream().map(ResponseNotificationDTO::toResponseNotificationDTO).collect(Collectors.toList());
    }
}
