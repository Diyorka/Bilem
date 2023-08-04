package kg.bilem.dto.commentReply;

import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.model.CommentReply;
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
public class ResponseCommentReplyDTO {
    Long id;

    String text;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    GetUserDTO userDTO;

    public static ResponseCommentReplyDTO toResponseCommentReplyDTO(CommentReply commentReply){
        return ResponseCommentReplyDTO.builder()
                .id(commentReply.getId())
                .text(commentReply.getText())
                .createdAt(commentReply.getCreatedAt())
                .updatedAt(commentReply.getUpdatedAt())
                .userDTO(toGetUserDto(commentReply.getUser()))
                .build();
    }

    public static List<ResponseCommentReplyDTO> toResponseCommentReplyDTO(Set<CommentReply> commentReplies){
        return commentReplies.stream().map(ResponseCommentReplyDTO::toResponseCommentReplyDTO).collect(Collectors.toList());
    }
}
