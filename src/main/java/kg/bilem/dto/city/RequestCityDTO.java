package kg.bilem.dto.city;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCityDTO {
    @NotBlank(message = "Название города не может быть пустым")
    String name;
}
