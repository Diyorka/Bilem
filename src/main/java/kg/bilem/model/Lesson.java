package kg.bilem.model;

import jakarta.persistence.*;
import kg.bilem.enums.LessonType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lesson extends BaseEntity{
    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    @Enumerated(EnumType.STRING)
    LessonType lessonType;

    String imageUrl;

    String videoUrl;

    String question;

    String correctAnswer;

    @ElementCollection
    @CollectionTable(name = "lesson_incorrect_answers", joinColumns = @JoinColumn(name = "lesson_id"))
    List<String> incorrectAnswers = new ArrayList<>();

    @ManyToOne
    Module module;
}
