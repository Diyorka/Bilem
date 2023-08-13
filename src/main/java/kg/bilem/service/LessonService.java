package kg.bilem.service;

import kg.bilem.dto.lesson.RequestLessonDTO;
import kg.bilem.dto.lesson.ResponseLessonDTO;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface LessonService {
    Page<ResponseLessonDTO> getLessonsByModuleId(Long moduleId, Pageable pageable, User user);

    ResponseLessonDTO getLessonById(Long id, User user);

    ResponseLessonDTO createLesson(RequestLessonDTO lessonDTO, User user);

    ResponseEntity<ResponseWithMessage> editLesson(Long lessonId, RequestLessonDTO lessonDTO, User user);

    ResponseEntity<ResponseWithMessage> deleteLesson(Long lessonId, User user);
}
