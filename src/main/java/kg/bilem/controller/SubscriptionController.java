package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.subscription.ResponseSubscriberDTO;
import kg.bilem.dto.subscription.ResponseSubscriptionDTO;
import kg.bilem.model.User;
import kg.bilem.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с подписками",
        description = "В этом контроллере есть возможность получения, добавления и удаления подписок"
)
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/my-subscriptions")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение всех подписок авторизованного пользователя"
    )
    public Page<ResponseSubscriptionDTO> getUsersSubscriptions(@PageableDefault Pageable pageable,
                                                               @AuthenticationPrincipal User subscriber){
        return subscriptionService.getUserSubscriptions(subscriber, pageable);
    }

    @GetMapping("/my-subscribers")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение всех подписчиков авторизованного пользователя"
    )
    public Page<ResponseSubscriberDTO> getUsersSubscribers(@PageableDefault Pageable pageable,
                                                             @AuthenticationPrincipal User user){
        return subscriptionService.getUserSubscribers(user, pageable);
    }

    @PostMapping("/subscribe")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Подписаться на пользователя"
    )
    public ResponseEntity<String> subscribeUser(@RequestParam Long userId,
                                                @AuthenticationPrincipal User subscriber){
        return subscriptionService.subscribeUser(userId, subscriber);
    }

    @DeleteMapping("/unsubscribe")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Отписаться от пользователя"
    )
    public ResponseEntity<String> unsubscribeUser(@RequestParam Long userId,
                                                  @AuthenticationPrincipal User subscriber){
        return subscriptionService.unsubscribeUser(userId, subscriber);
    }

    @DeleteMapping("/delete-subscriber")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удалить подписчика"
    )
    public ResponseEntity<String> deleteSubscriber(@RequestParam Long subscriberId,
                                                  @AuthenticationPrincipal User user){
        return subscriptionService.deleteSubscriber(subscriberId, user);
    }
}
