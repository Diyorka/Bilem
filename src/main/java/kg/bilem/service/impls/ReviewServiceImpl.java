package kg.bilem.service.impls;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.review.RequestReviewDTO;
import kg.bilem.dto.review.ResponseReviewDTO;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.*;
import kg.bilem.repository.CourseRepository;
import kg.bilem.repository.ReviewReplyRepository;
import kg.bilem.repository.ReviewRepository;
import kg.bilem.service.ReviewService;
import kg.bilem.service.StudentProgressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

import static kg.bilem.dto.review.ResponseReviewDTO.toResponseReviewDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    CourseRepository courseRepository;
    ReviewReplyRepository reviewReplyRepository;
    StudentProgressService studentProgressService;

    @Override
    public ResponseEntity<ResponseWithMessage> addReview(Long courseId,
                                                         RequestReviewDTO reviewDTO,
                                                         User user) {
        if (reviewRepository.existsByCourseIdAndUserId(courseId, user.getId())) {
            throw new AlreadyExistException("Этот пользователь уже оставил отзыв на данный курс");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с данным айди не найден"));

        if(studentProgressService.getStudentProgressPercentageOnCourse(courseId, user).getBody().getValue() < 70){
            throw new NoAccessException("Вы должны пройти 70% курса, чтобы оставить отзыв");
        }

        reviewRepository.save(constructReview(reviewDTO, user, course));
        updateCourseAverageScore(course);

        return ResponseEntity.ok(new ResponseWithMessage("Отзыв успешно добавлен"));
    }

    @Override
    public ResponseEntity<ResponseWithMessage> editReview(Long reviewId, RequestReviewDTO reviewDTO, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыв с таким айди не найден"));

        if (!user.getEmail().equals(review.getUser().getEmail())) {
            throw new NoAccessException("У вас нет прав на редактирование данного отзыва");
        }

        review.setText(reviewDTO.getText());
        review.setScore(reviewDTO.getScore());
        reviewRepository.save(review);

        return ResponseEntity.ok(new ResponseWithMessage("Отзыв успешно отредактирован"));
    }

    @Override
    public ResponseEntity<ResponseWithMessage> deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыв с таким айди не найден"));

        if (user.getRole() != Role.ADMIN || !user.getEmail().equals(review.getUser().getEmail())) {
            throw new NoAccessException("У вас нет прав на удаление данного отзыва");
        }

        List<ReviewReply> reviewReplies = reviewReplyRepository.findAllByReviewId(reviewId);
        reviewReplyRepository.deleteAll(reviewReplies);
        reviewRepository.delete(review);

        return ResponseEntity.ok(new ResponseWithMessage("Отзыв успешно удален"));
    }


    @Override
    public Page<ResponseReviewDTO> getReviewsByCourseId(Long courseId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByCourseId(courseId, pageable);
        List<ResponseReviewDTO> reviewDTOS = toResponseReviewDTO(reviews.toSet());
        return new PageImpl<>(reviewDTOS, pageable, reviews.getTotalElements());
    }

    private Review constructReview(RequestReviewDTO requestReviewDTO, User user, Course course) {
        return Review.builder()
                .score(requestReviewDTO.getScore())
                .text(requestReviewDTO.getText())
                .course(course)
                .user(user)
                .build();
    }

    private void updateCourseAverageScore(Course course) {
        List<Review> reviews = reviewRepository.findAllByCourseId(course.getId());
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getScore();
        }
        course.setAverageScore(Double.parseDouble(new DecimalFormat("0.0").format(sum / reviews.size())
                .replaceAll(",", ".")));
        courseRepository.save(course);
    }
}
