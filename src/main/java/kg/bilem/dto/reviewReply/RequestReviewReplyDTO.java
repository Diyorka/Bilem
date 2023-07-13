package kg.bilem.dto.reviewReply;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestReviewReplyDTO {
    @NotBlank(message = "Ответ на отзыв не может быть пустым")
    String text;
}
