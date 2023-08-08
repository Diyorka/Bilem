package kg.bilem.dto.module;

import kg.bilem.dto.course.ResponseCourseDTO;
import kg.bilem.dto.lesson.ResponseLessonDTO;
import kg.bilem.model.Module;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kg.bilem.dto.course.ResponseCourseDTO.toResponseCourseDTO;
import static kg.bilem.dto.lesson.ResponseLessonDTO.toResponseLessonDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseModuleDTO {
    Long id;

    String title;

    Long ordinalNumber;

    public static ResponseModuleDTO toResponseModuleDTO(Module module){
        return ResponseModuleDTO.builder()
                .id(module.getId())
                .title(module.getTitle())
                .ordinalNumber(module.getOrdinalNumber())
                .build();
    }

    public static List<ResponseModuleDTO> toResponseModuleDTO(Set<Module> modules){
        return modules.stream().map(ResponseModuleDTO::toResponseModuleDTO).collect(Collectors.toList());
    }
}
