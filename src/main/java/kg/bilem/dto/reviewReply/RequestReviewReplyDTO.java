package kg.bilem.dto.reviewReply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestReviewReplyDTO {
    @NotEmpty(message = "Ответ на отзыв не может быть пустым")
    String text;
}
