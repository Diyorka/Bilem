package kg.bilem.dto.mailing;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestMailingDTO {
    @Email(message = "Почта должна быть корректной")
    String email;
}
