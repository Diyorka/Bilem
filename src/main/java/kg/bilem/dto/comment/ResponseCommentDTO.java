package kg.bilem.dto.comment;

import kg.bilem.dto.commentReply.ResponseCommentReplyDTO;
import kg.bilem.dto.reviewReply.ResponseReviewReplyDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.model.Comment;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kg.bilem.dto.commentReply.ResponseCommentReplyDTO.toResponseCommentReplyDTO;
import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCommentDTO {
    Long id;

    String text;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    GetUserDTO userDTO;

    List<ResponseCommentReplyDTO> commentReplyDTOS;

    public static ResponseCommentDTO toResponseCommentDTO(Comment comment){
        return ResponseCommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .userDTO(toGetUserDto(comment.getUser()))
                .commentReplyDTOS(toResponseCommentReplyDTO(comment.getCommentReplies()))
                .build();
    }

    public static List<ResponseCommentDTO> toResponseCommentDTO(Set<Comment> comments){
        return comments.stream().map(ResponseCommentDTO::toResponseCommentDTO).collect(Collectors.toList());
    }
}
