package kg.bilem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "student_progress")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentProgress extends BaseEntity{
    @ManyToOne
    User student;

    @ManyToOne
    Lesson lesson;

    @Column(columnDefinition = "boolean default false")
    boolean completed;
}
