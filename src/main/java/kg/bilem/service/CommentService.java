package kg.bilem.service;

import kg.bilem.dto.comment.RequestCommentDTO;
import kg.bilem.dto.comment.ResponseCommentDTO;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<String> addComment(Long lessonId, RequestCommentDTO commentDTO, User user);

    ResponseEntity<String> editComment(Long commentId, RequestCommentDTO commentDTO, User user);

    ResponseEntity<String> deleteComment(Long commentId, User user);

    Page<ResponseCommentDTO> getCommentsByLessonId(Long lessonId, Pageable pageable);
}
