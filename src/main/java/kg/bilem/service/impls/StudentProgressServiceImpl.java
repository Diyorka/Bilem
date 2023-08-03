package kg.bilem.service.impls;

import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Lesson;
import kg.bilem.model.StudentProgress;
import kg.bilem.model.User;
import kg.bilem.repository.LessonRepository;
import kg.bilem.repository.StudentProgressRepository;
import kg.bilem.service.StudentProgressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StudentProgressServiceImpl implements StudentProgressService {
    StudentProgressRepository studentProgressRepository;
    LessonRepository lessonRepository;

    @Override
    public ResponseEntity<String> startLesson(Long lessonId, User user) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок с таким айди не найден"));

        if(!lesson.getModule().getCourse().getStudents().contains(user)){
            throw new NoAccessException("Вы не являетесь студентом данного курса");
        }

        StudentProgress studentProgress = StudentProgress.builder()
                .student(user)
                .lesson(lesson)
                .completed(false)
                .build();

        studentProgressRepository.save(studentProgress);
        return ResponseEntity.ok("Вы начали урок с айди " + lessonId);
    }

    @Override
    public ResponseEntity<String> completeLesson(Long lessonId, User user) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок с таким айди не найден"));

        if(!lesson.getModule().getCourse().getStudents().contains(user)){
            throw new NoAccessException("Вы не являетесь студентом данного курса");
        }

        StudentProgress studentProgress = studentProgressRepository.findByLessonIdAndStudentId(lessonId, user.getId())
                .orElseThrow(() -> new NotFoundException("Вы не начинали прохождение данного урока"));

        studentProgress.setCompleted(true);
        studentProgressRepository.save(studentProgress);
        return ResponseEntity.ok("Вы успешно завершили урок с айди " + lessonId);
    }
}
