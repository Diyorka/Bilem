package kg.bilem.enums;

import kg.bilem.exception.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum LessonType {
    TEXT("Текст"), VIDEO("Видео"), TEST("Тест");

    private final String lessonType;

    public static LessonType of(String lessonType) {
        return Stream.of(LessonType.values())
                .filter(l -> l.getLessonType().equals(lessonType))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Тип курса не найден"));
    }
}
