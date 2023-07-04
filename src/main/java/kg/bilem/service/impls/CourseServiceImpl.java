package kg.bilem.service.impls;

import kg.bilem.dto.course.CreateCourseDTO;
import kg.bilem.dto.course.GetCourseDTO;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Course;
import kg.bilem.model.Subcategory;
import kg.bilem.model.User;
import kg.bilem.repository.CourseRepository;
import kg.bilem.repository.SubcategoryRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubcategoryRepository subcategoryRepository;

    @Override
    public Page<GetCourseDTO> getCoursesBySubcategoryId(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<String> createCourse(CreateCourseDTO courseDTO, User user) {
        if (courseRepository.existsByName(courseDTO.getTitle())) {
            throw new AlreadyExistException("Курс с таким названием уже существует");
        }

        if (user.getRole() == Role.STUDENT) {
            user.setRole(Role.TEACHER);
            userRepository.save(user);
        }

        courseRepository.save(
                Course.builder()
                        .price(courseDTO.getPrice())
                        .title(courseDTO.getTitle())
                        .courseType(courseDTO.getCourseType())
                        .description(courseDTO.getDescription())
                        .imageUrl(courseDTO.getImageUrl())
                        .language(courseDTO.getLanguage())
                        .videoUrl(courseDTO.getVideoUrl())
                        .status(Status.NOT_ACTIVATED)
                        .owner(user)
                        .build()
        );

        return ResponseEntity.ok("Курс успешно создан");
    }

    @Override
    public GetCourseDTO getCourseByTitle(String titleOfCourse) {
        if (!courseRepository.existsByName(titleOfCourse)) {
            throw new NotFoundException("Курс с таким названием не существует");
        }
        return GetCourseDTO.toGetCourseDTO(courseRepository.findByName(titleOfCourse));
    }

}
