package kg.bilem.service;

import kg.bilem.dto.course.RequestCourseDTO;
import kg.bilem.dto.course.ResponseCourseDTO;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    ResponseCourseDTO createCourse(RequestCourseDTO courseDTO, User user);
    ResponseEntity<String> editCourse(Long courseId, RequestCourseDTO courseDTO, User user);

}
