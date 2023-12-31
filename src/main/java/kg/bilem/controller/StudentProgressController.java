package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.other.StudentProgressDTO;
import kg.bilem.model.User;
import kg.bilem.service.StudentProgressService;
import kg.bilem.service.impls.StudentProgressServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-progress")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с прогрессом студента на курсе",
        description = "В этом контроллере есть возможности начать, завершить уроки"
)
public class StudentProgressController {
    StudentProgressServiceImpl studentProgressService;

    @PostMapping("/start-lesson/{lesson_id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Указать урок как начатый"
    )
    public ResponseEntity<ResponseWithMessage> startLesson(@PathVariable Long lesson_id,
                                                           @AuthenticationPrincipal User user){
        return studentProgressService.startLesson(lesson_id, user);
    }

    @PutMapping("/complete-lesson/{lesson_id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Указать урок как завершенный"
    )
    public ResponseEntity<ResponseWithMessage> completeLesson(@PathVariable Long lesson_id,
                                                 @RequestParam(required = false) String testAnswer,
                                                 @AuthenticationPrincipal User user){
        return studentProgressService.completeLesson(lesson_id, testAnswer, user);
    }

    @GetMapping("/{courseId}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получить процент прогресса студента на курсе"
    )
    public ResponseEntity<StudentProgressDTO> getStudentProgressPercentageOnCourse(@PathVariable Long courseId,
                                                                                   @AuthenticationPrincipal User user){
        return studentProgressService.getStudentProgressPercentageOnCourse(courseId, user);
    }
}
