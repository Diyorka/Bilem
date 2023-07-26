package kg.bilem.service;

import kg.bilem.dto.course.RequestCourseDTO;
import kg.bilem.dto.course.ResponseCourseDTO;
import kg.bilem.dto.course.ResponseMainCourseDTO;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    ResponseCourseDTO createCourse(RequestCourseDTO courseDTO, User user);

    ResponseEntity<String> editCourse(Long courseId, RequestCourseDTO courseDTO, User user);

    ResponseCourseDTO getCourseById(Long courseId);

    Page<ResponseMainCourseDTO> getAllCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getTopCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getNewestAndFreeCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getPopularAndFreeCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getNewestAndPaidCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getPopularAndPaidCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getAllCoursesWithSearchByQueryAndLanguageAndCourseType(String query, String language, String courseType, Pageable pageable);

    ResponseEntity<String> approveCourse(Long courseId, User user);

    Page<ResponseMainCourseDTO> getCoursesOnChecking(Pageable pageable, User user);


}
