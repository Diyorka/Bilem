package kg.bilem.dto.favorite;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestFavoriteDTO {
    @NotNull(message = "Айди курса не может быть пустым")
    Long course_id;
}
