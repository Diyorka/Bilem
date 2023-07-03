package kg.bilem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    CourseType courseType;

    int price;

    Status status;

    Language language;

    @ManyToOne
    User owner;

    @ManyToMany(mappedBy = "teachingCourses")
    List<User> teachers;

    @ManyToMany(mappedBy = "studyingCourses")
    List<User> students;
}
