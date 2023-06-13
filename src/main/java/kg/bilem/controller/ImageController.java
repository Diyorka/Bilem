package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.model.User;
import kg.bilem.service.impls.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с фото",
        description = "В этом контроллеры есть возможности добавления фото"
)
public class ImageController {
    private final ImageServiceImpl imageService;
    @PostMapping(value = "/upload/myAvatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление фото профиля"
    )
    public ResponseEntity<String> saveUserImage(@AuthenticationPrincipal User user,
                                                @RequestPart MultipartFile file) throws IOException {
        return imageService.saveForUser(user, file);
    }
}
