package kg.bilem.model;

import jakarta.persistence.*;
import kg.bilem.enums.Language;
import kg.bilem.enums.Status;
import kg.bilem.enums.CourseType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "course")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course extends BaseEntity{
    String name;

    String imageUrl;

    String videoUrl;

    String description;

    @ManyToOne
    Subcategory subcategory;

    @Enumerated(EnumType.STRING)
    CourseType courseType;

    int price;

    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    Language language;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User owner;

    @ManyToMany(mappedBy = "teachingCourses")
    List<User> teachers;

    @ManyToMany(mappedBy = "studyingCourses")
    List<User> students;
}
