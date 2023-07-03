package kg.bilem.service;

import kg.bilem.dto.course.CreateCourseDTO;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    ResponseEntity<String> createCourse(CreateCourseDTO courseDTO, User user);
}
