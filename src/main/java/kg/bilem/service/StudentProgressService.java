package kg.bilem.service;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.other.StudentProgressDTO;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface StudentProgressService {
    ResponseEntity<ResponseWithMessage> startLesson(Long lessonId, User user);

    ResponseEntity<ResponseWithMessage> completeLesson(Long lessonId, String testAnswer, User user);

    ResponseEntity<StudentProgressDTO> getStudentProgressPercentageOnCourse(Long courseId, User user);
}
