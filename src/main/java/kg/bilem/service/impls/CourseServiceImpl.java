package kg.bilem.service.impls;

import kg.bilem.dto.course.RequestCourseDTO;
import kg.bilem.dto.course.ResponseCourseDTO;
import kg.bilem.dto.course.ResponseMainCourseDTO;
import kg.bilem.enums.CourseType;
import kg.bilem.enums.Language;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Course;
import kg.bilem.model.Mailing;
import kg.bilem.model.User;
import kg.bilem.repository.*;
import kg.bilem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kg.bilem.dto.course.ResponseCourseDTO.toResponseCourseDTO;
import static kg.bilem.dto.course.ResponseMainCourseDTO.toResponseMainCourseDTO;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final MailingRepository mailingRepository;
    private final EmailServiceImpl emailService;


    @Override
    public ResponseCourseDTO createCourse(RequestCourseDTO courseDTO, User user) {
        if (courseRepository.existsByTitle(courseDTO.getTitle())) {
            throw new AlreadyExistException("Курс с таким названием уже существует");
        }

        if (user.getRole() == Role.STUDENT) {
            user.setRole(Role.TEACHER);
            userRepository.save(user);
        }

        Course course = buildCourse(courseDTO, user);
        course.setStatus(Status.CHECKING);

        return toResponseCourseDTO(courseRepository.save(course));
    }

    @Override
    public ResponseEntity<String> editCourse(Long courseId, RequestCourseDTO courseDTO, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с айди " + courseId + " не найден"));

        if (user.getRole() != Role.ADMIN || !user.getEmail().equals(course.getOwner().getEmail())) {
            return ResponseEntity.badRequest().body("Вы не имеете права на редактирование данного курса");
        }

        course = buildCourse(courseDTO, user);
        course.setId(courseId);
        course.setStatus(Status.CHECKING);
        courseRepository.save(course);

        int coursesCount = course.getSubcategory().getCategory().getCoursesCount();
        course.getSubcategory().getCategory().setCoursesCount(coursesCount - 1);
        categoryRepository.save(course.getSubcategory().getCategory());

        return ResponseEntity.ok("Курс отправлен на модерацию");
    }

    @Override
    public ResponseCourseDTO getCourseById(Long courseId) {
        return toResponseCourseDTO(courseRepository.findById(courseId)
                .filter(course -> course.getStatus() == Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Курс с таким айди не найден"))
        );
    }

    @Override
    public Page<ResponseMainCourseDTO> getAllCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAllByStatus(Status.ACTIVE, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    @Override
    public Page<ResponseMainCourseDTO> getTopCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findTopCourses(Status.ACTIVE, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    @Override
    public Page<ResponseMainCourseDTO> getNewestAndFreeCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAllByStatusAndCourseTypeOrderByCreatedAtDesc(Status.ACTIVE, CourseType.FREE, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    @Override
    public Page<ResponseMainCourseDTO> getPopularAndFreeCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAllByStatusAndCourseTypeOrderByNumberOfStudentsDesc(Status.ACTIVE, CourseType.FREE, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    @Override
    public Page<ResponseMainCourseDTO> getNewestAndPaidCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAllByStatusAndCourseTypeOrderByCreatedAtDesc(Status.ACTIVE, CourseType.PAID, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    @Override
    public Page<ResponseMainCourseDTO> getPopularAndPaidCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAllByStatusAndCourseTypeOrderByNumberOfStudentsDesc(Status.ACTIVE, CourseType.PAID, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    @Override
    public Page<ResponseMainCourseDTO> getAllCoursesWithSearchByQueryAndLanguageAndCourseType(String query, String language, String courseType, Pageable pageable) {
        Page<Course> courses;

        if (query != null && language != null && courseType != null) {
            courses = courseRepository.findAllByStatusAndTitleContainsIgnoreCaseAndLanguageAndCourseType(Status.ACTIVE, query, Language.of(language), CourseType.of(courseType), pageable);
        } else if (query != null && language != null) {
            courses = courseRepository.findAllByStatusAndTitleContainsIgnoreCaseAndLanguage(Status.ACTIVE, query, Language.of(language), pageable);
        } else if (query != null && courseType != null) {
            courses = courseRepository.findAllByStatusAndTitleContainsIgnoreCaseAndCourseType(Status.ACTIVE, query, CourseType.of(courseType), pageable);
        } else if (language != null && courseType != null) {
            courses = courseRepository.findAllByStatusAndCourseTypeAndLanguage(Status.ACTIVE, CourseType.of(courseType), Language.of(language), pageable);
        } else if (query != null) {
            courses = courseRepository.findAllByStatusAndTitleContainsIgnoreCase(Status.ACTIVE, query, pageable);
        } else if (language != null) {
            courses = courseRepository.findAllByStatusAndLanguage(Status.ACTIVE, Language.of(language), pageable);
        } else if (courseType != null) {
            courses = courseRepository.findAllByStatusAndCourseTypeOrderByCreatedAtDesc(Status.ACTIVE, CourseType.of(courseType), pageable);
        } else {
            courses = courseRepository.findAllByStatus(Status.ACTIVE, pageable);
        }

        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }


    @Override
    public ResponseEntity<String> approveCourse(Long courseId, User user) {
        if (user.getRole() != Role.ADMIN) {
            throw new NoAccessException("У вас нет доступа на одобрение курсов");
        }

        Course course = courseRepository.findById(courseId)
                .filter(c -> c.getStatus() == Status.CHECKING)
                .orElseThrow(() -> new NotFoundException("Курс не существует либо уже активен"));

        course.setStatus(Status.ACTIVE);
        courseRepository.save(course);

        int coursesCount = course.getSubcategory().getCategory().getCoursesCount();
        course.getSubcategory().getCategory().setCoursesCount(coursesCount + 1);
        categoryRepository.save(course.getSubcategory().getCategory());

        sendMails();

        return ResponseEntity.ok("Курс успешно одобрен");
    }

    @Override
    public Page<ResponseMainCourseDTO> getCoursesOnChecking(Pageable pageable, User user) {
        Page<Course> courses = courseRepository.findAllByStatus(Status.CHECKING, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    private void sendMails() {
        List<Mailing> mailings = mailingRepository.findAll();
        for(Mailing mailing:mailings){
            SimpleMailMessage activationEmail = new SimpleMailMessage();
            activationEmail.setFrom("bilem@gmail.com");
            activationEmail.setTo(mailing.getEmail());
            activationEmail.setSubject("Bilem KG");
            activationEmail.setText("На нашей платформе был добавлен новый курс!\n" +
                    "Перейдите на сайт для того чтобы посмотреть более подробную информацию");

            emailService.sendEmail(activationEmail);
        }
    }

    private Course buildCourse(RequestCourseDTO courseDTO, User user) {
        Set<User> teachers = new HashSet<>();
        if (courseDTO.getTeacherIds() != null) {
            for (Long id : courseDTO.getTeacherIds()) {
                User teacher = userRepository.findById(id)
                        .filter(t -> t.getRole() == Role.TEACHER)
                        .orElseThrow(() -> new NotFoundException("Преподаватель с айди " + id + " не найден"));
                teachers.add(teacher);
            }
        }

        return Course.builder()
                .title(courseDTO.getTitle())
                .courseType(CourseType.of(courseDTO.getCourseType()))
                .videoUrl(courseDTO.getVideoUrl())
                .description(courseDTO.getDescription())
                .whatStudentGet(courseDTO.getWhatStudentGet())
                .price(CourseType.of(courseDTO.getCourseType()) == CourseType.PAID ? courseDTO.getPrice() : 0)
                .subcategory(subcategoryRepository.findById(courseDTO.getSubcategoryId())
                        .orElseThrow(() -> new NotFoundException("Подкатегория с таким айди не найдена")))
                .teachers(teachers)
                .students(new HashSet<>())
                .reviews(new HashSet<>())
                .owner(user)
                .averageScore((double) 0)
                .language(Language.of(courseDTO.getLanguage()))
                .build();

    }
}
