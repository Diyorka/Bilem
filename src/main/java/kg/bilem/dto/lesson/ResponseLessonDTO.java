package kg.bilem.dto.lesson;

import kg.bilem.dto.module.ResponseModuleDTO;
import kg.bilem.enums.LessonType;
import kg.bilem.model.Lesson;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kg.bilem.dto.module.ResponseModuleDTO.toResponseModuleDTO;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseLessonDTO {
    Long id;

    String title;

    String content;

    Long ordinalNumber;

    String imageUrl;

    String videoUrl;

    String lessonType;

    String question;

    String correctAnswer;

    List<String> incorrectAnswers;

    public static ResponseLessonDTO toResponseLessonDTO(Lesson lesson) {
        return ResponseLessonDTO.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .ordinalNumber(lesson.getOrdinalNumber())
                .imageUrl(lesson.getImageUrl())
                .videoUrl(lesson.getVideoUrl())
                .lessonType(lesson.getLessonType().name())
                .question(lesson.getQuestion())
                .correctAnswer(lesson.getCorrectAnswer())
                .incorrectAnswers(lesson.getIncorrectAnswers())
                .build();
    }

    public static List<ResponseLessonDTO> toResponseLessonDTO(Set<Lesson> lessons) {
        return lessons.stream().map(ResponseLessonDTO::toResponseLessonDTO).collect(Collectors.toList());
    }
}
