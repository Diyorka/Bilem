package kg.bilem.dto.subscription;

import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.model.Subscription;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseSubscriberDTO {
    GetUserDTO subscriber;

    LocalDateTime subscriptionDate;

    public static ResponseSubscriberDTO toResponseSubscriberDTO(Subscription subscription){
        return ResponseSubscriberDTO.builder()
                .subscriber(toGetUserDto(subscription.getSubscriber()))
                .subscriptionDate(subscription.getSubscriptionDate())
                .build();
    }

    public static List<ResponseSubscriberDTO> toResponseSubscriberDTO(List<Subscription> subscriptions){
        return subscriptions.stream().map(ResponseSubscriberDTO::toResponseSubscriberDTO).collect(Collectors.toList());
    }
}
