package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.course.ResponseMainCourseDTO;
import kg.bilem.model.Course;
import kg.bilem.model.User;
import kg.bilem.service.impls.CourseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы админа"
)
public class AdminController {
    private final CourseServiceImpl courseService;

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
}
