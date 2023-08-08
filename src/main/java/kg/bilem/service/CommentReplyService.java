package kg.bilem.service;

import kg.bilem.dto.commentReply.RequestCommentReplyDTO;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface CommentReplyService {
    ResponseEntity<String> addCommentReply(Long commentId, RequestCommentReplyDTO commentReplyDTO, User user);

    ResponseEntity<String> editCommentReply(Long commentReplyId, RequestCommentReplyDTO commentReplyDTO, User user);

    ResponseEntity<String> deleteCommentReply(Long commentReplyId, User user);
}
