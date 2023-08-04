package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.commentReply.RequestCommentReplyDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.CommentReplyServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment-reply")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с ответами на комментарии",
        description = "В этом контроллере есть возможность добавления, изменения и удаления ответов на комментарии"
)
public class CommentReplyController {
    CommentReplyServiceImpl commentReplyService;

    @PostMapping("/add/{commentId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление ответа на комментарий"
    )
    public ResponseEntity<String> addCommentReply(@PathVariable Long commentId,
                                                 @RequestBody @Valid RequestCommentReplyDTO commentReplyDTO,
                                                 @AuthenticationPrincipal User user){
        return commentReplyService.addCommentReply(commentId, commentReplyDTO, user);
    }

    @PostMapping("/edit/{commentReplyId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Редактирование ответа на комментарий"
    )
    public ResponseEntity<String> editCommentReply(@PathVariable Long commentReplyId,
                                                  @RequestBody RequestCommentReplyDTO commentReplyDTO,
                                                  @AuthenticationPrincipal User user){
        return commentReplyService.editCommentReply(commentReplyId, commentReplyDTO, user);
    }

    @DeleteMapping("/{commentReplyId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление ответа на комментарий"
    )
    public ResponseEntity<String> deleteCommentReply(@PathVariable Long commentReplyId,
                                                    @AuthenticationPrincipal User user){
        return commentReplyService.deleteCommentReply(commentReplyId, user);
    }
}
