package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.model.User;
import kg.bilem.service.impls.ImageServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с фото",
        description = "В этом контроллеры есть возможности добавления фото"
)
public class ImageController {
    ImageServiceImpl imageService;

    @PostMapping(value = "/upload/myAvatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление фото профиля"
    )
    public ResponseEntity<ResponseWithMessage> saveUserImage(@AuthenticationPrincipal User user,
                                                             @RequestPart MultipartFile file) throws IOException {
        return imageService.saveForUser(user, file);
    }

    @PostMapping(value = "/upload/course-image/{courseId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    @Operation(
            summary = "Добавление изображения курса"
    )
    public ResponseEntity<ResponseWithMessage> saveCourseImage(@PathVariable Long courseId,
                                                @RequestPart MultipartFile file,
                                                @AuthenticationPrincipal User user) throws IOException {
        return imageService.saveForCourse(courseId, file, user);
    }
}
