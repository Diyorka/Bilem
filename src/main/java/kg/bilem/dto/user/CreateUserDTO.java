package kg.bilem.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserDTO {
    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Введите электронную почту корректно")
    String email;

    @Size(min = 4, max = 30, message = "Имя должно содержать от 2 до 30 символов")
    String name;

    @Size(min = 4, max = 30, message = "Пароль должен содержать от 4 до 30 символов")
    String password;

    String confirmPassword;
}
