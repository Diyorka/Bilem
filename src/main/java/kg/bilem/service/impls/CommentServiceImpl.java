package kg.bilem.service.impls;

import kg.bilem.dto.comment.RequestCommentDTO;
import kg.bilem.dto.comment.ResponseCommentDTO;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.*;
import kg.bilem.repository.CommentReplyRepository;
import kg.bilem.repository.CommentRepository;
import kg.bilem.repository.LessonRepository;
import kg.bilem.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static kg.bilem.dto.comment.ResponseCommentDTO.toResponseCommentDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    LessonRepository lessonRepository;
    CommentReplyRepository commentReplyRepository;

    @Override
    public ResponseEntity<String> addComment(Long lessonId, RequestCommentDTO commentDTO, User user) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок с данным айди не найден"));

        commentRepository.save(constructComment(commentDTO, user, lesson));

        return ResponseEntity.ok("Комментарий успешно добавлен");
    }

    @Override
    public ResponseEntity<String> editComment(Long commentId, RequestCommentDTO commentDTO, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с данным айди не найден"));

        if (!user.getEmail().equals(comment.getUser().getEmail())) {
            throw new NoAccessException("У вас нет прав на редактирование данного комментария");
        }

        comment.setText(commentDTO.getText());
        commentRepository.save(comment);

        return ResponseEntity.ok("Комментарий успешно отредактирован");
    }

    @Override
    public ResponseEntity<String> deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с данным айди не найден"));

        if (!user.getEmail().equals(comment.getUser().getEmail())) {
            throw new NoAccessException("У вас нет прав на удаление данного комментария");
        }

        List<CommentReply> commentReplies = commentReplyRepository.findAllByCommentId(commentId);
        commentReplyRepository.deleteAll(commentReplies);
        commentRepository.delete(comment);

        return ResponseEntity.ok("Комментарий успешно удален");
    }

    @Override
    public Page<ResponseCommentDTO> getCommentsByLessonId(Long lessonId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByLessonId(lessonId, pageable);
        List<ResponseCommentDTO> commentDTO = toResponseCommentDTO(comments.toSet());
        return new PageImpl<>(commentDTO, pageable, comments.getTotalElements());
    }

    private Comment constructComment(RequestCommentDTO commentDTO, User user, Lesson lesson) {
        return Comment.builder()
                .text(commentDTO.getText())
                .lesson(lesson)
                .user(user)
                .build();
    }
}
