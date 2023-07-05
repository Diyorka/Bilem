package kg.bilem.enums;

import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Course;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum CourseType {
    FREE("Бесплатный"), PAID("Платный");

    private final String courseType;

    public static CourseType of(String courseType) {
        return Stream.of(CourseType.values())
                .filter(cType -> cType.getCourseType().equals(courseType))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Тип курса не найден"));
    }
}
