package kg.bilem.dto.lesson;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLessonDTO {
    @NotEmpty(message = "Название урока не может быть пустым")
    String title;

    String content;

    @NotEmpty(message = "Тип урока не может быть пустым")
    String lessonType;

    String imageUrl;

    String videoUrl;

    String question;

    String correctAnswer;

    List<String> incorrectAnswers;

    @NotNull(message = "Айди модуля не может быть пустым")
    Long moduleId;
}
