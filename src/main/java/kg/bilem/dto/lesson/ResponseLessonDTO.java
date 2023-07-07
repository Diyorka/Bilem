package kg.bilem.dto.lesson;

import kg.bilem.dto.module.ResponseModuleDTO;
import kg.bilem.enums.LessonType;
import kg.bilem.model.Lesson;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String title;

    String content;

    String videoUrl;

    String lessonType;

    ResponseModuleDTO moduleDTO;

    public static ResponseLessonDTO toResponseLessonDTO(Lesson lesson){
        return ResponseLessonDTO.builder()
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .videoUrl(lesson.getVideoUrl())
                .lessonType(lesson.getLessonType().name())
                .moduleDTO(toResponseModuleDTO(lesson.getModule()))
                .build();
    }

    public static List<ResponseLessonDTO> toResponseLessonDTO(Set<Lesson> lessons){
        return lessons.stream().map(ResponseLessonDTO::toResponseLessonDTO).collect(Collectors.toList());
    }
}
