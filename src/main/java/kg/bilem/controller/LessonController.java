package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.lesson.RequestLessonDTO;
import kg.bilem.dto.lesson.ResponseLessonDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.LessonServiceImpl;
import kg.bilem.service.impls.VideoServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/lesson")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с уроками",
        description = "В этом контроллере есть возможность получения, добавления и изменения уроков"
)
public class LessonController {
    LessonServiceImpl lessonService;
    VideoServiceImpl videoService;

    @GetMapping("/{module_id}/all")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение всех уроков по айди модуля"
    )
    public Page<ResponseLessonDTO> getLessonsByModuleId(@PathVariable Long module_id,
                                                        @PageableDefault Pageable pageable,
                                                        @AuthenticationPrincipal User user){
        return lessonService.getLessonsByModuleId(module_id, pageable, user);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение урока по айди"
    )
    public ResponseLessonDTO getLessonById(@PathVariable Long id,
                                           @AuthenticationPrincipal User user){
        return lessonService.getLessonById(id, user);
    }

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление урока"
    )
    public ResponseLessonDTO createLesson(@RequestBody @Valid RequestLessonDTO lessonDTO,
                                            @AuthenticationPrincipal User user){
        return lessonService.createLesson(lessonDTO, user);
    }

    @PostMapping(value = "/{lesson_id}/add-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление видео к уроку"
    )
    public ResponseEntity<String> addVideoForLesson(@PathVariable Long lesson_id,
                                                    @RequestParam(required = false) String videoUrl,
                                                    @RequestPart(required = false) MultipartFile file,
                                                    @AuthenticationPrincipal User user) throws IOException {
        return videoService.saveVideoForLesson(lesson_id, videoUrl, file, user);
    }


    @PutMapping("/{lesson_id}/edit")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Изменение урока"
    )
    public ResponseEntity<String> editLesson(@PathVariable Long lesson_id,
                                             @RequestBody @Valid RequestLessonDTO lessonDTO,
                                             @AuthenticationPrincipal User user){
        return lessonService.editLesson(lesson_id, lessonDTO, user);
    }

    @DeleteMapping("/{lesson_id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление урока"
    )
    public ResponseEntity<String> deleteLesson(@PathVariable Long lesson_id,
                                               @AuthenticationPrincipal User user){
        return lessonService.deleteLesson(lesson_id, user);
    }
}
