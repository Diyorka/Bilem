package kg.bilem.service.impls;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.enums.LessonType;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Course;
import kg.bilem.model.Lesson;
import kg.bilem.model.StudentProgress;
import kg.bilem.model.User;
import kg.bilem.repository.CourseRepository;
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
    CourseRepository courseRepository;

    @Override
    public ResponseEntity<ResponseWithMessage> startLesson(Long lessonId, User user) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок с таким айди не найден"));

        if (!lesson.getModule().getCourse().getStudents().contains(user)) {
            throw new NoAccessException("Вы не являетесь студентом данного курса");
        }

        StudentProgress studentProgress = StudentProgress.builder()
                .student(user)
                .lesson(lesson)
                .completed(false)
                .build();

        studentProgressRepository.save(studentProgress);
        return ResponseEntity.ok(new ResponseWithMessage("Вы начали урок с айди " + lessonId));
    }

    @Override
    public ResponseEntity<ResponseWithMessage> completeLesson(Long lessonId, String testAnswer, User user) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок с таким айди не найден"));

        if (!lesson.getModule().getCourse().getStudents().contains(user)) {
            throw new NoAccessException("Вы не являетесь студентом данного курса");
        }

        StudentProgress studentProgress = studentProgressRepository.findByLessonIdAndStudentId(lessonId, user.getId())
                .orElseThrow(() -> new NotFoundException("Вы не начинали прохождение данного урока"));

        if (lesson.getLessonType() == LessonType.TEST && !lesson.getCorrectAnswer().equals(testAnswer)) {
            return ResponseEntity.badRequest().body(new ResponseWithMessage("Ответ на тест неправильный"));
        }

        studentProgress.setCompleted(true);
        studentProgressRepository.save(studentProgress);
        return ResponseEntity.ok(new ResponseWithMessage("Вы успешно завершили урок"));
    }

    @Override
    public Integer getStudentProgressPercentageOnCourse(Long courseId, User user) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с таким айди не найден"));
        int totalLessonsCount = lessonRepository.getTotalLessonsCountForCourse(courseId);
        int studentCompletedLessonsCount = studentProgressRepository.getCompletedLessonsCountForStudentInCourse(user.getId(), courseId);

        if (totalLessonsCount == 0) {
            return 0;
        }

        return (int) (((double) studentCompletedLessonsCount / totalLessonsCount) * 100.0);
    }
}
