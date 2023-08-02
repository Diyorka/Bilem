package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.course.RequestCourseDTO;
import kg.bilem.dto.course.ResponseCourseDTO;
import kg.bilem.dto.course.ResponseMainCourseDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с курсами",
        description = "В этом контроллере есть возможность получения, добавления и изменения курсов"
)
public class CourseController {
    private final CourseServiceImpl courseService;

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Создание курса"
    )
    public ResponseCourseDTO createCourse(@RequestBody @Valid RequestCourseDTO courseDTO,
                                          @AuthenticationPrincipal User user) {
        return courseService.createCourse(courseDTO, user);
    }

    @PostMapping("/{course_id}/send-for-checking")
    @SecurityRequirement(name="JWT")
    @Operation(
            summary = "Отправка курса на проверку"
    )
    public ResponseEntity<String> sendCourseForChecking(@PathVariable Long course_id,
                                                  @AuthenticationPrincipal User user){
        return courseService.sendCourseForChecking(course_id, user);
    }

    @PutMapping("/edit/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Редактирование курса"
    )
    public ResponseEntity<String> editCourse(@PathVariable Long id,
                                             @RequestBody RequestCourseDTO courseDTO,
                                             @AuthenticationPrincipal User user) {
        return courseService.editCourse(id, courseDTO, user);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение курса по айди"
    )
    public ResponseCourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Получение всех курсов с основной информацией"
    )
    public Page<ResponseMainCourseDTO> getAllCourses(@PageableDefault Pageable pageable) {
        return courseService.getAllCourses(pageable);
    }

    @GetMapping("/my-courses")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    @Operation(
            summary = "Получение курсов преподавателя"
    )
    public Page<ResponseMainCourseDTO> getCoursesOfTeacher(@PageableDefault Pageable pageable,
                                                           @AuthenticationPrincipal User user){
        return courseService.getCoursesOfTeacher(pageable, user);
    }

    @GetMapping("/my-studying-courses")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение курсов студента"
    )
    public Page<ResponseMainCourseDTO> getCoursesOfStudent(@PageableDefault Pageable pageable,
                                                           @AuthenticationPrincipal User user){
        return courseService.getCoursesOfStudent(pageable, user);
    }

    @GetMapping("/top")
    @Operation(
            summary = "Получение лучших курсов по количеству студентов"
    )
    public Page<ResponseMainCourseDTO> getTopCourses(@PageableDefault Pageable pageable) {
        return courseService.getTopCourses(pageable);
    }

    @GetMapping("/newest-and-free-courses")
    @Operation(
            summary = "Получение всех новейших бесплатных курсов"
    )
    public Page<ResponseMainCourseDTO> getNewestAndFreeCourses(@PageableDefault Pageable pageable) {
        return courseService.getNewestAndFreeCourses(pageable);
    }

    @GetMapping("/popular-and-free-courses")
    @Operation(
            summary = "Получение всех популярных бесплатных курсов"
    )
    public Page<ResponseMainCourseDTO> getPopularAndFreeCourses(@PageableDefault Pageable pageable) {
        return courseService.getPopularAndFreeCourses(pageable);
    }

    @GetMapping("/newest-and-paid-courses")
    @Operation(
            summary = "Получение всех новейших платных курсов"
    )
    public Page<ResponseMainCourseDTO> getNewestAndPaidCourses(@PageableDefault Pageable pageable) {
        return courseService.getNewestAndPaidCourses(pageable);
    }

    @GetMapping("/popular-and-paid-courses")
    @Operation(
            summary = "Получение всех популярных платных курсов"
    )
    public Page<ResponseMainCourseDTO> getPopularAndPaidCourses(@PageableDefault Pageable pageable) {
        return courseService.getPopularAndPaidCourses(pageable);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Поиск курса по названию, языку и типу"
    )
    public Page<ResponseMainCourseDTO> getAllCoursesWithSearchByQueryAndLanguageAndCourseType(@RequestParam(required = false) String query,
                                                                                              @RequestParam(required = false) String language,
                                                                                              @RequestParam(required = false) String courseType,
                                                                                              @PageableDefault Pageable pageable) {
        return courseService.getAllCoursesWithSearchByQueryAndLanguageAndCourseType(query, language, courseType, pageable);
    }

}
