package kg.bilem.service;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.review.RequestReviewDTO;
import kg.bilem.dto.review.ResponseReviewDTO;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface ReviewService {
    ResponseEntity<ResponseWithMessage> addReview(Long courseId, RequestReviewDTO reviewDTO, User user);

    ResponseEntity<ResponseWithMessage> editReview(Long reviewId, RequestReviewDTO reviewDTO, User user);

    ResponseEntity<ResponseWithMessage> deleteReview(Long reviewId, User user);

    Page<ResponseReviewDTO> getReviewsByCourseId(Long courseId, Pageable pageable);
}
