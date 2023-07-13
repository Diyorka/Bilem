package kg.bilem.dto.review;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestReviewDTO {
    @Size(min = 3, message = "Отзыв должен содержать от 3 символов")
    String text;

    @NotNull(message = "Оценка не может быть пустой")
    @Min(value = 1, message = "Минимальная оценка 1")
    @Max(value = 5, message = "Максимальная оценка 5")
    int score;
}
