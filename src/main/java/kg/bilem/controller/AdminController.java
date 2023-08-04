package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.course.ResponseMainCourseDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.model.Course;
import kg.bilem.model.User;
import kg.bilem.service.impls.CourseServiceImpl;
import kg.bilem.service.impls.UserServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы админа"
)
public class AdminController {
    CourseServiceImpl courseService;
    UserServiceImpl userService;

    @GetMapping("/all")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Получение всех пользователей"
    )
    public Page<GetUserDTO> getAllUsers(@PageableDefault Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/all/active")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Получение активных пользователей"
    )
    public Page<GetUserDTO> getAllActiveUsers(@PageableDefault Pageable pageable) {
        return userService.getAllActiveUsers(pageable);
    }

    @GetMapping("/all/students")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Получение активных студентов"
    )
    public Page<GetUserDTO> getAllStudents(@PageableDefault Pageable pageable) {
        return userService.getAllStudents(pageable);
    }

    @GetMapping("/all/teachers")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Получение активных преподавателей"
    )
    public Page<GetUserDTO> getAllTeachers(@PageableDefault Pageable pageable) {
        return userService.getAllTeachers(pageable);
    }

    @GetMapping("/checking-courses")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Получить курсы, которые отправлены на проверку"
    )
    public Page<ResponseMainCourseDTO> getCoursesOnChecking(@PageableDefault Pageable pageable,
                                                            @AuthenticationPrincipal User user){
        return courseService.getCoursesOnChecking(pageable, user);
    }

    @PutMapping("/approve-course/{courseId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Одобрить курс"
    )
    public ResponseEntity<String> approveCourse(@PathVariable Long courseId,
                                                @AuthenticationPrincipal User user){
        return courseService.approveCourse(courseId, user);
    }

    @PostMapping("/addAdmin")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Добавление нового администратора"
    )
    public ResponseEntity<String> addAdmin(@RequestBody @Valid CreateUserDTO userDto) {
        return userService.addAdmin(userDto);
    }
}
