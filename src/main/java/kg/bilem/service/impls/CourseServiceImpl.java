package kg.bilem.service.impls;

import kg.bilem.dto.course.CreateCourseDTO;
import kg.bilem.dto.course.EditCourseDTO;
import kg.bilem.dto.course.GetCourseDTO;
import kg.bilem.enums.CourseType;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Course;
import kg.bilem.model.User;
import kg.bilem.repository.CourseRepository;
import kg.bilem.repository.SubcategoryRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubcategoryRepository subcategoryRepository;

    @Override
    public ResponseEntity<String> createCourse(CreateCourseDTO courseDTO, User user) {
        if (courseRepository.existsByName(courseDTO.getName())) {
            throw new AlreadyExistException("Курс с таким названием уже существует");
        }

        if (user.getRole() == Role.STUDENT) {
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

    @Override
    public ResponseEntity<String> editCourse(Long courseId, EditCourseDTO courseDTO, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с айди " + courseId + " не найден"));

        if(user.getRole() != Role.ADMIN || !user.equals(course.getOwner())){
            return ResponseEntity.badRequest().body("Вы не имеете права на редактирование данного курса");
        }

        updateCourse(course, courseDTO);
        course.setStatus(Status.CHECKING);
        courseRepository.save(course);

        return ResponseEntity.ok("Курс отправлен на модерацию");
    }

    private void updateCourse(Course course, EditCourseDTO courseDTO) {
        course.setCourseType(CourseType.of(courseDTO.getCourseType()));
        course.setImageUrl(courseDTO.getImageUrl());
        course.setVideoUrl(courseDTO.getVideoUrl());
        course.setDescription(courseDTO.getDescription());
        course.setWhatStudentGet(courseDTO.getWhatStudentGet());
        course.setPrice(course.getCourseType() == CourseType.PAID ? courseDTO.getPrice() : 0);
        course.setSubcategory(subcategoryRepository.findById(courseDTO.getSubcategoryId())
                .orElseThrow(() -> new NotFoundException("Подкатегория с таким айди не найдена")));

        List<User> teachers = new ArrayList<>();
        for (Long id: courseDTO.getTeacherIds()) {
            User teacher = userRepository.findById(id)
                    .filter(t -> t.getRole() == Role.TEACHER)
                    .orElseThrow(() -> new NotFoundException("Преподаватель с айди " + id + " не найден"));
            teachers.add(teacher);
        }
        course.setTeachers(teachers);
    }
}
