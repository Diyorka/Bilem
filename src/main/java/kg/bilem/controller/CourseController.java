package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.course.CreateCourseDTO;
import kg.bilem.dto.course.GetCourseDTO;
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

    final CourseServiceImpl courseService;

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление нового курса"
    )
    public ResponseEntity<String> addCourse(@AuthenticationPrincipal User user, @RequestBody CreateCourseDTO courseDTO) {
        return courseService.createCourse(courseDTO, user);
    }

    @GetMapping("/get/{titleOfCourse}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение курса по названию"
    )
    public GetCourseDTO getCourseByTitle(@PathVariable String titleOfCourse) {
        return courseService.getCourseByTitle(titleOfCourse);
    }

}
