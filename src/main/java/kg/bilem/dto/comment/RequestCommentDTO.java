package kg.bilem.dto.comment;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCommentDTO {
    @Size(min = 3, message = "Комментарий должен содержать от 3 символов")
    String text;
}
