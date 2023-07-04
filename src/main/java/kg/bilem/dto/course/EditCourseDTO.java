package kg.bilem.dto.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditCourseDTO {
    String imageUrl;

    String videoUrl;

    String description;

    String whatStudentGet;

    List<Long> teacherIds;

    Long subcategoryId;

    String courseType;

    int price;
}
