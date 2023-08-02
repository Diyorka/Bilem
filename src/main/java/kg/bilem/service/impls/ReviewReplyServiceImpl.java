package kg.bilem.service.impls;

import kg.bilem.dto.reviewReply.RequestReviewReplyDTO;
import kg.bilem.enums.Status;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.*;
import kg.bilem.repository.ReviewReplyRepository;
import kg.bilem.repository.ReviewRepository;
import kg.bilem.service.ReviewReplyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewReplyServiceImpl implements ReviewReplyService {
    ReviewRepository reviewRepository;
    ReviewReplyRepository reviewReplyRepository;

    @Override
    public ResponseEntity<String> addReviewReply(Long reviewId, RequestReviewReplyDTO reviewReplyDTO, User user) {
        Review review = reviewRepository.findById(reviewId)
                .filter(r -> r.getStatus() == Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Отзыв с данным айди не найден"));

        Course course = review.getCourse();
        if(!course.getTeachers().contains(user) && !user.getEmail().equals(course.getOwner().getEmail())){
            throw new NoAccessException("Вы не можете отвечать на отзывы данного курса");
        }

        sendNotification(review.getUser(), reviewReplyDTO, course);
        reviewReplyRepository.save(constructReviewReply(reviewReplyDTO, review, user));

        return ResponseEntity.ok("Ответ на отзыв успешно добавлен");
    }

    private void sendNotification(User user, RequestReviewReplyDTO reviewReplyDTO, Course course) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setHeader("Получен ответ на отзыв!");
        notification.setMessage("Вы получили ответ на свой отзыв под курсом '" + course.getTitle() +
                "'.\nТекст ответа: " + reviewReplyDTO.getText());
        notification.setStatus(Status.ACTIVE);

    }

    @Override
    public ResponseEntity<String> editReviewReply(Long reviewReplyId, RequestReviewReplyDTO reviewReplyDTO, User user) {
        ReviewReply reviewReply = reviewReplyRepository.findById(reviewReplyId)
                .filter(r -> r.getStatus() == Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Ответ на отзыв с таким айди не найден"));

        if(!user.getEmail().equals(reviewReply.getUser().getEmail())){
            throw new NoAccessException("У вас нет доступа на редактирование данного ответа");
        }

        reviewReply.setText(reviewReplyDTO.getText());
        reviewReplyRepository.save(reviewReply);

        return ResponseEntity.ok("Ответ на отзыв успешно обновлен");
    }

    @Override
    public ResponseEntity<String> deleteReviewReply(Long reviewReplyId, User user) {
        ReviewReply reviewReply = reviewReplyRepository.findById(reviewReplyId)
                .filter(r -> r.getStatus() == Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Ответ на отзыв с таким айди не найден"));

        if(!user.getEmail().equals(reviewReply.getUser().getEmail())){
            throw new NoAccessException("У вас нет доступа на редактирование данного ответа");
        }

        reviewReply.setStatus(Status.DELETED);
        reviewReplyRepository.save(reviewReply);

        return ResponseEntity.ok("Ответ на отзыв успешно удален");
    }

    private ReviewReply constructReviewReply(RequestReviewReplyDTO reviewReplyDTO, Review review, User user) {
        return ReviewReply.builder()
                .text(reviewReplyDTO.getText())
                .review(review)
                .user(user)
                .status(Status.ACTIVE)
                .build();
    }
}
