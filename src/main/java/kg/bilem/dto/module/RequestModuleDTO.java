package kg.bilem.dto.module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Название модуля не может быть пустым")
    String title;

    @NotNull(message = "Порядковый номер модуля не может быть пустым")
    Long ordinalNumber;

    @NotNull(message = "Айди курса не может быть пустым")
    Long courseId;
}
