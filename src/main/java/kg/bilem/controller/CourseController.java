package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.course.RequestCourseDTO;
import kg.bilem.dto.course.ResponseCourseDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseCourseDTO createCourse(@RequestBody RequestCourseDTO courseDTO,
                                          @AuthenticationPrincipal User user) {
        return courseService.createCourse(courseDTO, user);
    }

    @PutMapping("/edit/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Редактирование курса"
    )
    public ResponseEntity<String> editCourse(@PathVariable Long id, 
                                             @RequestBody RequestCourseDTO courseDTO,
                                             @AuthenticationPrincipal User user){
        return courseService.editCourse(id, courseDTO, user);
    }

}
