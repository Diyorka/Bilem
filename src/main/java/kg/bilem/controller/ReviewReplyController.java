package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.reviewReply.RequestReviewReplyDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.ReviewReplyServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review-reply")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с ответами на отзывы",
        description = "В этом контроллере есть возможность добавления, изменения и удаления ответов на отзывы"
)
public class ReviewReplyController {
    ReviewReplyServiceImpl reviewReplyService;

    @PostMapping("/add/{reviewId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('TEACHER')")
    @Operation(
            summary = "Добавление ответа на отзыв"
    )
    public ResponseEntity<ResponseWithMessage> addReviewReply(@PathVariable Long reviewId,
                                                              @RequestBody @Valid RequestReviewReplyDTO reviewReplyDTO,
                                                              @AuthenticationPrincipal User user){
        return reviewReplyService.addReviewReply(reviewId, reviewReplyDTO, user);
    }

    @PostMapping("/edit/{reviewReplyId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('TEACHER')")
    @Operation(
            summary = "Редактирование ответа на отзыв"
    )
    public ResponseEntity<ResponseWithMessage> editReviewReply(@PathVariable Long reviewReplyId,
                                                 @RequestBody RequestReviewReplyDTO reviewReplyDTO,
                                                 @AuthenticationPrincipal User user){
        return reviewReplyService.editReviewReply(reviewReplyId, reviewReplyDTO, user);
    }

    @DeleteMapping("/{reviewReplyId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('TEACHER')")
    @Operation(
            summary = "Удаление ответа на отзыв"
    )
    public ResponseEntity<ResponseWithMessage> deleteReviewReply(@PathVariable Long reviewReplyId,
                                                   @AuthenticationPrincipal User user){
        return reviewReplyService.deleteReviewReply(reviewReplyId, user);
    }
}
