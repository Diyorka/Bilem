package kg.bilem.dto.course;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import kg.bilem.dto.subcategory.RequestSubcategoryDTO;
import kg.bilem.dto.subcategory.ResponseSubcategoryDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.enums.CourseType;
import kg.bilem.enums.Language;
import kg.bilem.enums.Status;
import kg.bilem.model.Course;
import kg.bilem.model.Subcategory;
import kg.bilem.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

import static kg.bilem.dto.subcategory.ResponseSubcategoryDTO.toResponseSubcategoryDTO;
import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetCourseDTO {
    Long id;

    String name;

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

    List<GetUserDTO> students;

    int studentsCount;

    public static GetCourseDTO toGetCourseDTO(Course course) {
        return GetCourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
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
                .students(toGetUserDto(course.getStudents()))
                .studentsCount(course.getStudents().size())
                .build();
    }

    public static List<GetCourseDTO> toGetCourseDTO(List<Course> courses) {
        return courses.stream().map(GetCourseDTO::toGetCourseDTO).collect(Collectors.toList());
    }
}
