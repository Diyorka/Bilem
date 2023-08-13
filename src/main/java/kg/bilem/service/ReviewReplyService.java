package kg.bilem.service;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.reviewReply.RequestReviewReplyDTO;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface ReviewReplyService {
    ResponseEntity<ResponseWithMessage> addReviewReply(Long reviewId, RequestReviewReplyDTO reviewReplyDTO, User user);

    ResponseEntity<ResponseWithMessage> editReviewReply(Long reviewReplyId, RequestReviewReplyDTO reviewReplyDTO, User user);

    ResponseEntity<ResponseWithMessage> deleteReviewReply(Long reviewReplyId, User user);
}
