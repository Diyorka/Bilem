package kg.bilem.service.impls;

import kg.bilem.dto.lesson.RequestLessonDTO;
import kg.bilem.dto.lesson.ResponseLessonDTO;
import kg.bilem.enums.LessonType;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Lesson;
import kg.bilem.model.Module;
import kg.bilem.model.User;
import kg.bilem.repository.LessonRepository;
import kg.bilem.repository.ModuleRepository;
import kg.bilem.service.LessonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static kg.bilem.dto.lesson.ResponseLessonDTO.toResponseLessonDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    LessonRepository lessonRepository;
    ModuleRepository moduleRepository;

    @Override
    public Page<ResponseLessonDTO> getLessonsByModuleId(Long moduleId, Pageable pageable, User user) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Модуль с таким айди не найден"));

        if (!user.getStudyingCourses().contains(module.getCourse()) && !user.getEmail().equals(module.getCourse().getOwner().getEmail())) {
            throw new NoAccessException("Вы не проходите данный курс");
        }

        Page<Lesson> lessons = lessonRepository.findAllByModuleId(moduleId, pageable);
        List<ResponseLessonDTO> lessonDTOS = toResponseLessonDTO(lessons.toSet());
        return new PageImpl<>(lessonDTOS, pageable, lessons.getTotalElements());
    }

    @Override
    public ResponseLessonDTO createLesson(RequestLessonDTO lessonDTO, User user) {
        Module module = moduleRepository.findById(lessonDTO.getModuleId())
                .orElseThrow(() -> new NotFoundException("Модуль с таким айди не найден"));

        if (!user.getEmail().equals(module.getCourse().getOwner().getEmail())) {
            throw new NoAccessException("У вас нет доступа к созданию урока в данном курсе");
        }

        if(lessonRepository.existsByTitleAndModuleId(lessonDTO.getTitle(), lessonDTO.getModuleId())){
            throw new AlreadyExistException("Урок с таким названием уже существует");
        }

        if(lessonRepository.existsByOrdinalNumberAndModuleId(lessonDTO.getOrdinalNumber(), lessonDTO.getModuleId())){
            throw new AlreadyExistException("Урок с таким порядковым номером уже существует");
        }

        Lesson lesson = Lesson.builder()
                .title(lessonDTO.getTitle())
                .module(module)
                .ordinalNumber(lessonDTO.getOrdinalNumber())
                .lessonType(LessonType.of(lessonDTO.getLessonType()))
                .content(lessonDTO.getLessonType().equals("Текст") ? lessonDTO.getContent() : null)
                .imageUrl(lessonDTO.getLessonType().equals("Текст") ? lessonDTO.getImageUrl() : null)
                .correctAnswer(lessonDTO.getLessonType().equals("Тест") ? lessonDTO.getCorrectAnswer() : null)
                .question(lessonDTO.getLessonType().equals("Тест") ? lessonDTO.getQuestion() : null)
                .incorrectAnswers(lessonDTO.getLessonType().equals("Тест") ? lessonDTO.getIncorrectAnswers() : null)
                .build();

        return toResponseLessonDTO(lessonRepository.save(lesson));
    }

    @Override
    public ResponseEntity<String> editLesson(Long lessonId, RequestLessonDTO lessonDTO, User user) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок с таким айди не найден"));

        if (!user.getEmail().equals(lesson.getModule().getCourse().getOwner().getEmail())) {
            throw new NoAccessException("Вы не имеете доступ к изменению данного урока");
        }

        lesson.setLessonType(LessonType.of(lessonDTO.getLessonType()));
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setContent(lessonDTO.getLessonType().equals("TEXT") ? lessonDTO.getContent() : null);
        lesson.setImageUrl(lessonDTO.getLessonType().equals("TEXT") ? lessonDTO.getImageUrl() : null);
        lesson.setCorrectAnswer(lessonDTO.getLessonType().equals("TEST") ? lessonDTO.getCorrectAnswer() : null);
        lesson.setQuestion(lessonDTO.getLessonType().equals("TEST") ? lessonDTO.getQuestion() : null);
        lesson.setIncorrectAnswers(lessonDTO.getLessonType().equals("TEST") ? lessonDTO.getIncorrectAnswers() : null);

        lessonRepository.save(lesson);

        return ResponseEntity.ok("Урок успешно отредактирован");
    }

    @Override
    public ResponseEntity<String> deleteLesson(Long lessonId, User user) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок с таким айди не найден"));

        if (!user.getEmail().equals(lesson.getModule().getCourse().getOwner().getEmail())) {
            throw new NoAccessException("У вас нет доступа к удалению данного урока");
        }

        lessonRepository.delete(lesson);
        return ResponseEntity.ok("Урок успешно удален");
    }

    @Override
    public ResponseLessonDTO getLessonById(Long id, User user) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Урок с таким айди не найден"));

        if (user.getStudyingCourses().contains(lesson.getModule().getCourse())){
            throw new NoAccessException("Вы не являетесь студентом данного курса");
        }

        return toResponseLessonDTO(lesson);
    }
}
