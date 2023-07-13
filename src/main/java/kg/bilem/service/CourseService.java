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

}
