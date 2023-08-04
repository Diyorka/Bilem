package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.comment.RequestCommentDTO;
import kg.bilem.dto.comment.ResponseCommentDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.CommentServiceImpl;
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
@RequestMapping("/api/comment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с комментариями к урокам",
        description = "В этом контроллере есть возможность получения, добавления, изменения и удаления комментариев"
)
public class CommentController {
    CommentServiceImpl commentService;

    @PostMapping("/add/{lessonId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление комментария"
    )
    public ResponseEntity<String> addComment(@PathVariable Long lessonId,
                                            @RequestBody @Valid RequestCommentDTO commentDTO,
                                            @AuthenticationPrincipal User user) {
        return commentService.addComment(lessonId, commentDTO, user);
    }

    @PutMapping("/edit/{commentId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Редактирование комментария"
    )
    public ResponseEntity<String> editComment(@PathVariable Long commentId,
                                             @RequestBody RequestCommentDTO commentDTO,
                                             @AuthenticationPrincipal User user) {
        return commentService.editComment(commentId, commentDTO, user);
    }

    @DeleteMapping("/{commentId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление комментария"
    )
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                               @AuthenticationPrincipal User user) {
        return commentService.deleteComment(commentId, user);
    }

    @GetMapping("/all/{lessonId}")
    @Operation(
            summary = "Получение комментариев по айди урока"
    )
    public Page<ResponseCommentDTO> getAllCommentsByLessonId(@PathVariable Long lessonId,
                                                             @PageableDefault Pageable pageable) {
        return commentService.getCommentsByLessonId(lessonId, pageable);
    }
}
