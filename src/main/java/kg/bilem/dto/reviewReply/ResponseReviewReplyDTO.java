package kg.bilem.dto.reviewReply;

import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.model.ReviewReply;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseReviewReplyDTO {
    Long id;

    String text;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    GetUserDTO userDTO;

    public static ResponseReviewReplyDTO toResponseReviewReplyDTO(ReviewReply reviewReply){
        return ResponseReviewReplyDTO.builder()
                .id(reviewReply.getId())
                .text(reviewReply.getText())
                .createdAt(reviewReply.getCreatedAt())
                .updatedAt(reviewReply.getUpdatedAt())
                .userDTO(toGetUserDto(reviewReply.getUser()))
                .build();
    }

    public static List<ResponseReviewReplyDTO> toResponseReviewReplyDTO(Set<ReviewReply> reviewReplies){
        return reviewReplies.stream().map(ResponseReviewReplyDTO::toResponseReviewReplyDTO).collect(Collectors.toList());
    }
}
