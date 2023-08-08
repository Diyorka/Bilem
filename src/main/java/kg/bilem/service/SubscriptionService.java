package kg.bilem.service;

import kg.bilem.dto.subscription.ResponseSubscriberDTO;
import kg.bilem.dto.subscription.ResponseSubscriptionDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SubscriptionService {
    ResponseEntity<String> subscribeUser(Long userId, User subscriber);

    ResponseEntity<String> unsubscribeUser(Long userId, User subscriber);

    Page<ResponseSubscriptionDTO> getUserSubscriptions(User user, Pageable pageable);

    Page<ResponseSubscriberDTO> getUserSubscribers(User user, Pageable pageable);

    ResponseEntity<String> deleteSubscriber(Long subscriberId, User user);
}
