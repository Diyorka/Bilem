package kg.bilem.service;

import kg.bilem.dto.course.CreateCourseDTO;
import kg.bilem.dto.course.GetCourseDTO;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    Page<GetCourseDTO> getCoursesBySubcategoryId(Long id);
    ResponseEntity<String> createCourse(CreateCourseDTO courseDTO, User user);
    GetCourseDTO getCourseByTitle(String titleOfCourse);
}
