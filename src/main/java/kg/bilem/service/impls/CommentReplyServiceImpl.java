package kg.bilem.service.impls;

import kg.bilem.dto.comment.RequestCommentDTO;
import kg.bilem.dto.commentReply.RequestCommentReplyDTO;
import kg.bilem.dto.reviewReply.RequestReviewReplyDTO;
import kg.bilem.enums.Status;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.*;
import kg.bilem.repository.CommentReplyRepository;
import kg.bilem.repository.CommentRepository;
import kg.bilem.repository.NotificationRepository;
import kg.bilem.service.CommentReplyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentReplyServiceImpl implements CommentReplyService {
    CommentRepository commentRepository;
    CommentReplyRepository commentReplyRepository;
    NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<String> addCommentReply(Long commentId, RequestCommentReplyDTO commentReplyDTO, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с данным айди не найден"));

        Lesson lesson = comment.getLesson();

        sendNotification(comment.getUser(), commentReplyDTO, lesson);
        commentReplyRepository.save(constructCommentReply(commentReplyDTO, comment, user));

        return ResponseEntity.ok("Ответ на отзыв успешно добавлен");
    }

    @Override
    public ResponseEntity<String> editCommentReply(Long commentReplyId, RequestCommentReplyDTO commentReplyDTO, User user) {
        CommentReply commentReply = commentReplyRepository.findById(commentReplyId)
                .orElseThrow(() -> new NotFoundException("Ответ на комментарий с таким айди не найден"));

        if(!user.getEmail().equals(commentReply.getUser().getEmail())){
            throw new NoAccessException("У вас нет доступа на редактирование данного ответа");
        }

        commentReply.setText(commentReplyDTO.getText());
        commentReplyRepository.save(commentReply);

        return ResponseEntity.ok("Ответ на комментарий успешно обновлен");
    }

    @Override
    public ResponseEntity<String> deleteCommentReply(Long commentReplyId, User user) {
        CommentReply commentReply = commentReplyRepository.findById(commentReplyId)
                .orElseThrow(() -> new NotFoundException("Ответ на комментарий с таким айди не найден"));

        if(!user.getEmail().equals(commentReply.getUser().getEmail())){
            throw new NoAccessException("У вас нет доступа на удаление данного ответа");
        }

        commentReplyRepository.delete(commentReply);
        return ResponseEntity.ok("Ответ на комментарий успешно удален");
    }

    private void sendNotification(User user, RequestCommentReplyDTO commentReplyDTO, Lesson lesson) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setHeader("Получен ответ на комментарий!");
        notification.setMessage("Вы получили ответ на свой комментарий под курсом '" + lesson.getTitle() +
                "'.\nТекст ответа: " + commentReplyDTO.getText());
        notification.setStatus(Status.ACTIVE);
        notificationRepository.save(notification);
    }

    private CommentReply constructCommentReply(RequestCommentReplyDTO commentReplyDTO, Comment comment, User user) {
        return CommentReply.builder()
                .text(commentReplyDTO.getText())
                .comment(comment)
                .user(user)
                .build();
    }
}
