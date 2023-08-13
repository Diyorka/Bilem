package kg.bilem.service;

import kg.bilem.dto.course.RequestCourseDTO;
import kg.bilem.dto.course.ResponseCourseDTO;
import kg.bilem.dto.course.ResponseMainCourseDTO;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.other.SignedUp;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    ResponseEntity<ResponseWithMessage> signUpForCourse(Long courseId, User user);

    ResponseEntity<ResponseWithMessage> leaveCourse(Long courseId, User user);

    ResponseEntity<ResponseWithMessage> sendCourseForChecking(Long courseId, User user);

    ResponseCourseDTO createCourse(RequestCourseDTO courseDTO, User user);

    ResponseEntity<ResponseWithMessage> editCourse(Long courseId, RequestCourseDTO courseDTO, User user);

    ResponseEntity<ResponseWithMessage> archiveCourse(Long courseId, User user);

    ResponseEntity<ResponseWithMessage> deleteCourse(Long courseId, User user);

    ResponseCourseDTO getCourseById(Long courseId);

    Page<ResponseMainCourseDTO> getAllCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getTopCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getNewestAndFreeCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getPopularAndFreeCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getNewestAndPaidCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getPopularAndPaidCourses(Pageable pageable);

    Page<ResponseMainCourseDTO> getAllCoursesWithSearchByQueryAndLanguageAndCourseType(String query, String language, String courseType, Pageable pageable);

    ResponseEntity<ResponseWithMessage> approveCourse(Long courseId, User user);

    ResponseEntity<ResponseWithMessage> rejectCourse(Long courseId, String reason, User user);

    Page<ResponseMainCourseDTO> getCoursesOnChecking(Pageable pageable, User user);

    Page<ResponseMainCourseDTO> getCoursesOfTeacher(Pageable pageable, User user);

    Page<ResponseMainCourseDTO> getCoursesOfStudent(Pageable pageable, User user);

    SignedUp isSignedUp(Long courseId, User user);
}
