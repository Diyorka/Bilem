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
import kg.bilem.model.Notification;
import kg.bilem.model.User;
import kg.bilem.repository.*;
import kg.bilem.service.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kg.bilem.dto.course.ResponseCourseDTO.toResponseCourseDTO;
import static kg.bilem.dto.course.ResponseMainCourseDTO.toResponseMainCourseDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    CourseRepository courseRepository;
    ModuleRepository moduleRepository;
    UserRepository userRepository;
    SubcategoryRepository subcategoryRepository;
    CategoryRepository categoryRepository;
    MailingRepository mailingRepository;
    EmailServiceImpl emailService;
    NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<String> signUpForCourse(Long courseId, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с таким айди не найден"));

        if(course.getStudents().contains(user)){
            throw new AlreadyExistException("Вы уже записаны на данный курс");
        }

        course.getStudents().add(user);
        courseRepository.save(course);
        return ResponseEntity.ok("Вы успешно записались на курс");
    }

    @Override
    public ResponseEntity<String> leaveCourse(Long courseId, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с таким айди не найден"));

        if(!course.getStudents().contains(user)){
            throw new NoAccessException("Вы не записаны на данный курс");
        }

        course.getStudents().remove(user);
        courseRepository.save(course);
        return ResponseEntity.ok("Вы успешно удалили курс из вашего списка курсов");
    }

    @Override
    public ResponseEntity<String> sendCourseForChecking(Long courseId, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с таким айди не найден"));

        if(!user.getEmail().equals(course.getOwner().getEmail())){
            throw new NoAccessException("Вы не имеете доступа к данному курсу");
        }

        if(!moduleRepository.existsByCourseId(courseId)){
            return ResponseEntity.badRequest().body("Ваш курс не содержит модулей");
        }

        if(course.getStatus() == Status.CHECKING){
            return ResponseEntity.badRequest().body("Курс уже не проверке");
        }

        course.setStatus(Status.CHECKING);
        courseRepository.save(course);
        return ResponseEntity.ok("Курс отправлен на проверку");
    }

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
        course.setStatus(Status.NOT_READY);

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
    public ResponseEntity<String> archiveCourse(Long courseId, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с айди " + courseId + " не найден"));

        if (user.getRole() != Role.ADMIN || !user.getEmail().equals(course.getOwner().getEmail())) {
            return ResponseEntity.badRequest().body("Вы не имеете права на редактирование данного курса");
        }

        course.setStatus(Status.ARCHIVED);
        courseRepository.save(course);

        return ResponseEntity.ok("Статус курса изменен");
    }

    @Override
    public ResponseEntity<String> deleteCourse(Long courseId, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с айди " + courseId + " не найден"));

        if (user.getRole() != Role.ADMIN || !user.getEmail().equals(course.getOwner().getEmail())) {
            return ResponseEntity.badRequest().body("Вы не имеете права на редактирование данного курса");
        }

        course.setStatus(Status.DELETED);
        courseRepository.save(course);

        return ResponseEntity.ok("Курс удален");
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
                .orElseThrow(() -> new NotFoundException("Курс не на проверке"));

        course.setStatus(Status.ACTIVE);
        courseRepository.save(course);

        int coursesCount = course.getSubcategory().getCategory().getCoursesCount();
        course.getSubcategory().getCategory().setCoursesCount(coursesCount + 1);
        categoryRepository.save(course.getSubcategory().getCategory());

        String header = "Ваш курс одобрен!";
        String message = "Ваш курс под названием '" + course.getTitle() + "' был одобрен!";
        sendNotification(course, header, message);
        sendMails();

        return ResponseEntity.ok("Курс успешно одобрен");
    }

    @Override
    public ResponseEntity<String> rejectCourse(Long courseId, String reason, User user) {
        if (user.getRole() != Role.ADMIN) {
            throw new NoAccessException("У вас нет доступа на отклонение курсов");
        }

        Course course = courseRepository.findById(courseId)
                .filter(c -> c.getStatus() == Status.CHECKING)
                .orElseThrow(() -> new NotFoundException("Курс не на проверке"));

        course.setStatus(Status.NOT_READY);
        courseRepository.save(course);

        String header = "Ваш курс отклонен";
        String message = "Ваш курс под названием '" + course.getTitle() + "' был отклонен";
        sendNotification(course, header, message);
        sendMails();

        return ResponseEntity.ok("Курс успешно отклонен");
    }

    private void sendNotification(Course course, String header, String message) {
        Notification notification = new Notification();
        notification.setUser(course.getOwner());
        notification.setHeader(header);
        notification.setMessage(message);
        notification.setStatus(Status.ACTIVE);
        notificationRepository.save(notification);
    }

    @Override
    public Page<ResponseMainCourseDTO> getCoursesOnChecking(Pageable pageable, User user) {
        Page<Course> courses = courseRepository.findAllByStatus(Status.CHECKING, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    @Override
    public Page<ResponseMainCourseDTO> getCoursesOfTeacher(Pageable pageable, User owner) {
        Page<Course> courses = courseRepository.findAllByOwnerAndStatusIsNot(owner, Status.DELETED, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    @Override
    public Page<ResponseMainCourseDTO> getCoursesOfStudent(Pageable pageable, User student) {
        Page<Course> courses = courseRepository.findAllByStudentsContains(student, pageable);
        List<ResponseMainCourseDTO> courseDTOS = toResponseMainCourseDTO(courses.toList());
        return new PageImpl<>(courseDTOS, pageable, courses.getTotalElements());
    }

    private void sendMails() {
        List<Mailing> mailings = mailingRepository.findAll();
        for (Mailing mailing : mailings) {
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
