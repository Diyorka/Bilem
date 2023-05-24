package kg.bilem.dto.user;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordDTO {
    String oldPassword;

    @Size(min = 4, max = 30, message = "Пароль должен содержать от 4 до 30 символов")
    String newPassword;

    String confirmNewPassword;
}
