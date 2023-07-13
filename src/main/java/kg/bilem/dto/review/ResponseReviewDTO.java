package kg.bilem.dto.review;

import kg.bilem.dto.reviewReply.ResponseReviewReplyDTO;
import kg.bilem.dto.subcategory.ResponseSubcategoryDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.model.Review;
import kg.bilem.model.Subcategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kg.bilem.dto.category.ResponseCategoryDTO.toResponseCategoryDTO;
import static kg.bilem.dto.reviewReply.ResponseReviewReplyDTO.toResponseReviewReplyDTO;
import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseReviewDTO {
    Long id;

    String text;

    int score;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    GetUserDTO userDTO;

    List<ResponseReviewReplyDTO> reviewReplyDTOS;

    public static ResponseReviewDTO toResponseReviewDTO(Review review){
        return ResponseReviewDTO.builder()
                .id(review.getId())
                .text(review.getText())
                .score(review.getScore())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .userDTO(toGetUserDto(review.getUser()))
                .reviewReplyDTOS(toResponseReviewReplyDTO(review.getReviewReplies()))
                .build();
    }

    public static List<ResponseReviewDTO> toResponseReviewDTO(Set<Review> reviews){
        return reviews.stream().map(ResponseReviewDTO::toResponseReviewDTO).collect(Collectors.toList());
    }
}
