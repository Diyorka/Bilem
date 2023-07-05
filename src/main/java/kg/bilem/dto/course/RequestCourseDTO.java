package kg.bilem.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCourseDTO {
    @NotBlank(message = "Название курса не может быть пустым")
    String title;

    String imageUrl;

    String videoUrl;

    @NotBlank(message = "Описание не может быть пустым")
    String description;

    @NotBlank(message = "Что студент получит от курса не может быть пустым")
    String whatStudentGet;

    List<Long> teacherIds;

    @NotNull(message = "Подкатегория не может быть пустой")
    Long subcategoryId;

    @NotBlank(message = "Тип курса не может быть пустым")
    String courseType;

    @NotBlank(message = "Язык курса не может быть пустым")
    String language;

    int price;
}
