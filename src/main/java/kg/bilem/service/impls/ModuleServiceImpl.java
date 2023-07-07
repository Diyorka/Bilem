package kg.bilem.service.impls;

import kg.bilem.dto.module.RequestModuleDTO;
import kg.bilem.dto.module.ResponseModuleDTO;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Course;
import kg.bilem.model.User;
import kg.bilem.model.Module;
import kg.bilem.repository.CourseRepository;
import kg.bilem.repository.ModuleRepository;
import kg.bilem.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static kg.bilem.dto.module.ResponseModuleDTO.toResponseModuleDTO;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    @Override
    public ResponseModuleDTO createModule(RequestModuleDTO moduleDTO, User user) {
        Course course = courseRepository.findById(moduleDTO.getCourseId())
                .orElseThrow(() -> new NotFoundException("Курс с таким айди не найден"));

        if(moduleRepository.existsByTitleAndCourseId(moduleDTO.getTitle(), course.getId())){
            throw new AlreadyExistException("Модуль с таким названием уже существует");
        }

        if(!user.getEmail().equals(course.getOwner().getEmail())){
            throw new NoAccessException("Вы не имеете доступ к созданию нового модуля данного курса");
        }

        return toResponseModuleDTO(moduleRepository.save(Module.builder()
                        .course(course)
                        .title(moduleDTO.getTitle())
                .build())
        );
    }
}
