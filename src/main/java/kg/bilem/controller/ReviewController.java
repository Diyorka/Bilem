package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.review.RequestReviewDTO;
import kg.bilem.dto.review.ResponseReviewDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.ReviewServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с отзывами к курсам",
        description = "В этом контроллере есть возможность получения, добавления, изменения и удаления отзывов"
)
public class ReviewController {
    ReviewServiceImpl reviewService;

    @PostMapping("/add/{courseId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление отзыва"
    )
    public ResponseEntity<ResponseWithMessage> addReview(@PathVariable Long courseId,
                                                         @RequestBody @Valid RequestReviewDTO reviewDTO,
                                                         @AuthenticationPrincipal User user) {
        return reviewService.addReview(courseId, reviewDTO, user);
    }

    @PutMapping("/edit/{reviewId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Редактирование отзыва"
    )
    public ResponseEntity<ResponseWithMessage> editReview(@PathVariable Long reviewId,
                                             @RequestBody RequestReviewDTO reviewDTO,
                                             @AuthenticationPrincipal User user) {
        return reviewService.editReview(reviewId, reviewDTO, user);
    }

    @DeleteMapping("/{reviewId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление отзыва"
    )
    public ResponseEntity<ResponseWithMessage> deleteReview(@PathVariable Long reviewId,
                                               @AuthenticationPrincipal User user) {
        return reviewService.deleteReview(reviewId, user);
    }

    @GetMapping("/all/{courseId}")
    @Operation(
            summary = "Получение отзывов по айди курса"
    )
    public Page<ResponseReviewDTO> getAllReviewsByCourseId(@PathVariable Long courseId,
                                                           @PageableDefault Pageable pageable) {
        return reviewService.getReviewsByCourseId(courseId, pageable);
    }
}
