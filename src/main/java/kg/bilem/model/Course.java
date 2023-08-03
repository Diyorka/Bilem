package kg.bilem.model;

import jakarta.persistence.*;
import kg.bilem.enums.Language;
import kg.bilem.enums.Status;
import kg.bilem.enums.CourseType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> students;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    Set<Review> reviews;

    Double averageScore;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

}
