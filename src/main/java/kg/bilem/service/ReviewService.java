package kg.bilem.service;

import kg.bilem.dto.review.RequestReviewDTO;
import kg.bilem.dto.review.ResponseReviewDTO;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface ReviewService {
    ResponseEntity<String> addReview(Long courseId, RequestReviewDTO reviewDTO, User user);

    ResponseEntity<String> editReview(Long reviewId, RequestReviewDTO reviewDTO, User user);

    ResponseEntity<String> deleteReview(Long reviewId, User user);

    Page<ResponseReviewDTO> getReviewsByCourseId(Long courseId, Pageable pageable);
}
