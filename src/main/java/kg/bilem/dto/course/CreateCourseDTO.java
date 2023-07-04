package kg.bilem.dto.course;

import jakarta.validation.constraints.NotNull;
import kg.bilem.enums.CourseType;
import kg.bilem.enums.Language;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCourseDTO {
    @NotNull(message = "Название курса не может быть пустым")
    String title;

    int price;
    CourseType courseType;
    String description;
    String imageUrl;
    Language language;
    String videoUrl;
}
