package kg.bilem.service.impls;

import kg.bilem.dto.subscription.ResponseSubscriberDTO;
import kg.bilem.dto.subscription.ResponseSubscriptionDTO;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Subscription;
import kg.bilem.model.User;
import kg.bilem.repository.SubscriptionRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.SubscriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static kg.bilem.dto.course.ResponseMainCourseDTO.toResponseMainCourseDTO;
import static kg.bilem.dto.subscription.ResponseSubscriberDTO.toResponseSubscriberDTO;
import static kg.bilem.dto.subscription.ResponseSubscriptionDTO.toResponseSubscriptionDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    UserRepository userRepository;
    SubscriptionRepository subscriptionRepository;

    @Override
    public ResponseEntity<String> subscribeUser(Long userId, User subscriber) {
        User user = userRepository.findById(userId)
                .filter(u -> u.getStatus() == Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким айди не найден"));

        if(subscriptionRepository.existsByUserIdAndSubscriberId(userId, subscriber.getId())){
            throw new AlreadyExistException("Вы уже подписаны на данного пользователя");
        }
        if(userId.equals(subscriber.getId())){
            throw new NoAccessException("Вы не можете подписаться на самого себя");
        }

        subscriptionRepository.save(Subscription.builder()
                        .subscriber(subscriber)
                        .user(user)
                .build()
        );
        return ResponseEntity.ok("Вы успешно подписались на данного пользователя");
    }

    @Override
    public ResponseEntity<String> unsubscribeUser(Long userId, User subscriber) {
        Subscription subscription = subscriptionRepository.findByUserIdAndSubscriberId(userId, subscriber.getId())
                        .orElseThrow(() -> new NotFoundException("Вы не подписаны на данного пользователя"));

        subscriptionRepository.delete(subscription);
        return ResponseEntity.ok("Вы успешно отписались от данного пользователя");
    }

    @Override
    public Page<ResponseSubscriptionDTO> getUserSubscriptions(User subscriber, Pageable pageable) {
        Page<Subscription> subscriptions = subscriptionRepository.findAllBySubscriberId(subscriber.getId(), pageable);
        List<ResponseSubscriptionDTO> subscriptionDTOS = toResponseSubscriptionDTO(subscriptions.toList());
        return new PageImpl<>(subscriptionDTOS, pageable, subscriptions.getTotalElements());
    }

    @Override
    public Page<ResponseSubscriberDTO> getUserSubscribers(User user, Pageable pageable) {
        Page<Subscription> subscribers = subscriptionRepository.findAllByUserId(user.getId(), pageable);
        List<ResponseSubscriberDTO> subscriptionDTOS = toResponseSubscriberDTO(subscribers.toList());
        return new PageImpl<>(subscriptionDTOS, pageable, subscribers.getTotalElements());
    }

    @Override
    public ResponseEntity<String> deleteSubscriber(Long subscriberId, User user) {
        Subscription subscription = subscriptionRepository.findByUserIdAndSubscriberId(user.getId(), subscriberId)
                .orElseThrow(() -> new NotFoundException("Данный пользователь не подписан на вас"));

        subscriptionRepository.delete(subscription);
        return ResponseEntity.ok("Вы успешно отписали данного пользователя");
    }
}
