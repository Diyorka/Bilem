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
public class ResponseSubscriptionDTO {
    GetUserDTO user;

    LocalDateTime subscriptionDate;

    public static ResponseSubscriptionDTO toResponseSubscriptionDTO(Subscription subscription){
        return ResponseSubscriptionDTO.builder()
                .user(toGetUserDto(subscription.getUser()))
                .subscriptionDate(subscription.getSubscriptionDate())
                .build();
    }

    public static List<ResponseSubscriptionDTO> toResponseSubscriptionDTO(List<Subscription> subscriptions){
        return subscriptions.stream().map(ResponseSubscriptionDTO::toResponseSubscriptionDTO).collect(Collectors.toList());
    }
}
