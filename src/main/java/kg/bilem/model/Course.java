package kg.bilem.model;

import jakarta.persistence.*;
import kg.bilem.enums.Language;
import kg.bilem.enums.Status;
import kg.bilem.enums.CourseType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course extends BaseEntity{
    String title;

    String imageUrl;

    String videoUrl;

    String description;

    String whatStudentGet;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> teachers;

    @ManyToMany(mappedBy = "studyingCourses", fetch = FetchType.EAGER)
    Set<User> students;

}
