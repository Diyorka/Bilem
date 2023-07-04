package kg.bilem.service;

import kg.bilem.dto.course.CreateCourseDTO;
import kg.bilem.dto.course.EditCourseDTO;
import kg.bilem.dto.course.GetCourseDTO;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    ResponseEntity<String> createCourse(CreateCourseDTO courseDTO, User user);
    ResponseEntity<String> editCourse(Long courseId, EditCourseDTO courseDTO, User user);

}
