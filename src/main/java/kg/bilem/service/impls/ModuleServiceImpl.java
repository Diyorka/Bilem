package kg.bilem.service.impls;

import kg.bilem.dto.module.RequestModuleDTO;
import kg.bilem.dto.module.ResponseModuleDTO;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Course;
import kg.bilem.model.Lesson;
import kg.bilem.model.User;
import kg.bilem.model.Module;
import kg.bilem.repository.CourseRepository;
import kg.bilem.repository.LessonRepository;
import kg.bilem.repository.ModuleRepository;
import kg.bilem.service.ModuleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static kg.bilem.dto.module.ResponseModuleDTO.toResponseModuleDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    ModuleRepository moduleRepository;
    CourseRepository courseRepository;
    LessonRepository lessonRepository;

    @Override
    public ResponseModuleDTO createModule(RequestModuleDTO moduleDTO, User user) {
        Course course = courseRepository.findById(moduleDTO.getCourseId())
                .orElseThrow(() -> new NotFoundException("Курс с таким айди не найден"));

        if (!user.getEmail().equals(course.getOwner().getEmail())) {
            throw new NoAccessException("Вы не имеете доступ к созданию нового модуля данного курса");
        }

        if (moduleRepository.existsByTitleAndCourseId(moduleDTO.getTitle(), course.getId())) {
            throw new AlreadyExistException("Модуль с таким названием уже существует");
        }

        if(moduleRepository.existsByOrdinalNumberAndCourseId(moduleDTO.getOrdinalNumber(), course.getId())){
            throw new AlreadyExistException("Модуль с таким порядковым номером уже существует");
        }

        return toResponseModuleDTO(moduleRepository.save(Module.builder()
                .course(course)
                .ordinalNumber(moduleDTO.getOrdinalNumber())
                .title(moduleDTO.getTitle())
                .build())
        );
    }

    @Override
    public ResponseEntity<String> editModule(Long moduleId, RequestModuleDTO moduleDTO, User user) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Модуль с таким айди не найден"));

        if (!module.getCourse().getOwner().getEmail().equals(user.getEmail())) {
            throw new NoAccessException("Вы не имеете доступа к данному модулю");
        }

        module.setOrdinalNumber(moduleDTO.getOrdinalNumber());
        module.setTitle(moduleDTO.getTitle());
        moduleRepository.save(module);

        return ResponseEntity.ok("Модуль успешно отредактирован");
    }

    @Override
    public ResponseEntity<String> deleteModule(Long moduleId, User user) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Модуль с таким айди не найден"));
        if (!user.getEmail().equals(module.getCourse().getOwner().getEmail())) {
            throw new NoAccessException("Вы не имеете доступа к данному модулю");
        }

        List<Lesson> lessons = lessonRepository.findAllByModuleId(moduleId);
        lessonRepository.deleteAll(lessons);
        moduleRepository.delete(module);

        return ResponseEntity.ok("Модуль и все его данные успешно удалены");
    }


    @Override
    public Page<ResponseModuleDTO> getModulesByCourseId(Long courseId, Pageable pageable, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с таким айди не найден"));

        if(course.getStudents().contains(user)) {
            throw new NoAccessException("Вы не проходите данный курс");
        }

        Page<Module> modules = moduleRepository.findAllByCourseId(courseId, pageable);
        List<ResponseModuleDTO> moduleDTOS = toResponseModuleDTO(modules.toSet());
        return new PageImpl<>(moduleDTOS, pageable, modules.getTotalElements());
    }
}
