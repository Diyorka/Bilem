package kg.bilem.service;

import kg.bilem.dto.reviewReply.RequestReviewReplyDTO;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface ReviewReplyService {
    ResponseEntity<String> addReviewReply(Long reviewId, RequestReviewReplyDTO reviewReplyDTO, User user);

    ResponseEntity<String> editReviewReply(Long reviewReplyId, RequestReviewReplyDTO reviewReplyDTO, User user);

    ResponseEntity<String> deleteReviewReply(Long reviewReplyId, User user);
}
