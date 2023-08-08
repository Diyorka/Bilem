package kg.bilem.dto.course;

import kg.bilem.dto.module.ResponseModuleDTO;
import kg.bilem.dto.review.ResponseReviewDTO;
import kg.bilem.dto.subcategory.ResponseSubcategoryDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.enums.CourseType;
import kg.bilem.model.Course;
import kg.bilem.model.Review;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

import static kg.bilem.dto.module.ResponseModuleDTO.toResponseModuleDTO;
import static kg.bilem.dto.review.ResponseReviewDTO.toResponseReviewDTO;
import static kg.bilem.dto.subcategory.ResponseSubcategoryDTO.toResponseSubcategoryDTO;
import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCourseDTO {
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

    GetUserDTO owner;

    List<GetUserDTO> teachers;

    List<ResponseReviewDTO> reviews;

    int reviewsCount;

    int studentsCount;

    Double averageScore;


    public static ResponseCourseDTO toResponseCourseDTO(Course course) {
        return ResponseCourseDTO.builder()
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
                .owner(toGetUserDto(course.getOwner()))
                .teachers(toGetUserDto(course.getTeachers()))
                .studentsCount(course.getStudents().size())
                .averageScore(course.getAverageScore())
                .reviews(toResponseReviewDTO(course.getReviews()))
                .reviewsCount(course.getReviews().size())
                .build();
    }

    public static List<ResponseCourseDTO> toResponseCourseDTO(List<Course> courses) {
        return courses.stream().map(ResponseCourseDTO::toResponseCourseDTO).collect(Collectors.toList());
    }
}
