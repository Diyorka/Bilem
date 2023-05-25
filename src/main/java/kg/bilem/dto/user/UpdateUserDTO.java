package kg.bilem.dto.user;


import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserDTO {
    String name;

    @Email(message = "Почта некорректна")
    String email;

    String about_me;

    String profile_description;

    String activity_sphere;

    Long cityId;

    String work_place;

    String instagram;

    String github;

    String behance;

    String twitter;

    String youtube;

    String telegram;

    String dribble;
}
