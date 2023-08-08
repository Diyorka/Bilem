package kg.bilem.service;

import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface StudentProgressService {
    ResponseEntity<String> startLesson(Long lessonId, User user);

    ResponseEntity<String> completeLesson(Long lessonId, String testAnswer, User user);

    Integer getStudentProgressPercentageOnCourse(Long courseId, User user);
}
