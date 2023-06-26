package kg.bilem.dto.subcategory;

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
public class RequestSubcategoryDTO {
    @NotBlank(message = "Название подкатегории не может быть пустым")
    String name;

    @NotNull(message = "Айди категории не может быть пустым")
    Long categoryId;
}
