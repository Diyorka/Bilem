package kg.bilem.service.impls;

import kg.bilem.dto.course.CreateCourseDTO;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.model.Course;
import kg.bilem.model.User;
import kg.bilem.repository.CourseRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<String> createCourse(CreateCourseDTO courseDTO, User user) {
        if(courseRepository.existsByName(courseDTO.getName())){
            throw new AlreadyExistException("Курс с таким названием уже существует");
        }

        if(user.getRole() == Role.STUDENT){
            user.setRole(Role.TEACHER);
            userRepository.save(user);
        }

        courseRepository.save(
                 Course.builder()
                         .name(courseDTO.getName())
                         .status(Status.NOT_ACTIVATED)
                         .owner(user)
                         .build()
         );

         return ResponseEntity.ok("Курс успешно создан");
    }
}
