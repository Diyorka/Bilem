package kg.bilem.dto.course;

import kg.bilem.dto.subcategory.ResponseSubcategoryDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.enums.CourseType;
import kg.bilem.model.Course;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

import static kg.bilem.dto.review.ResponseReviewDTO.toResponseReviewDTO;
import static kg.bilem.dto.subcategory.ResponseSubcategoryDTO.toResponseSubcategoryDTO;
import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseMainCourseDTO {
    Long id;

    String title;

    String imageUrl;

    String videoUrl;

    String description;

    ResponseSubcategoryDTO subcategory;

    String courseType;

    int price;

    String status;

    String language;

    int reviewsCount;

    int studentsCount;

    Double averageScore;

    public static ResponseMainCourseDTO toResponseMainCourseDTO(Course course) {
        return ResponseMainCourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .imageUrl(course.getImageUrl())
                .videoUrl(course.getVideoUrl())
                .description(course.getDescription())
                .subcategory(toResponseSubcategoryDTO(course.getSubcategory()))
                .courseType(course.getCourseType().name())
                .price(course.getCourseType() == CourseType.PAID ? course.getPrice() : 0)
                .status(course.getStatus().name())
                .language(course.getLanguage().name())
                .studentsCount(course.getStudents().size())
                .averageScore(course.getAverageScore())
                .reviewsCount(course.getReviews().size())
                .build();
    }

    public static List<ResponseMainCourseDTO> toResponseMainCourseDTO(List<Course> courses) {
        return courses.stream().map(ResponseMainCourseDTO::toResponseMainCourseDTO).collect(Collectors.toList());
    }

}
