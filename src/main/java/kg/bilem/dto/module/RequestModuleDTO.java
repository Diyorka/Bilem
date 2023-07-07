package kg.bilem.dto.module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestModuleDTO {
    @NotBlank(message = "Название модуля не может быть пустым")
    String title;

    @NotNull(message = "Айди курса не может быть пустым")
    Long courseId;
}
