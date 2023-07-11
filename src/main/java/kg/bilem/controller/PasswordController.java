package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.user.ChangePasswordDTO;
import kg.bilem.dto.user.ResetPasswordDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.PasswordServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с паролями",
        description = "В этом контроллере есть возможность восстановления и смены пароля"
)
public class PasswordController {
    private final PasswordServiceImpl passwordService;

    @PostMapping("/forgot")
    @Operation(
            summary = "Восстановление пароля по почте"
    )
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return passwordService.forgotPassword(email);
    }

    @PostMapping("/reset/{token}")
    @Operation(
            summary = "Сброс пароля и создание нового пароля с помощью кода"
    )
    public ResponseEntity<String> resetPassword(@PathVariable("token") String token,
                                                @RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        return passwordService.setNewPassword(token, resetPasswordDTO);
    }

    @PostMapping("/change")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Смена пароля авторизованного пользователя"
    )
    public ResponseEntity<String> changePasswordOfUser(@RequestBody @Valid ChangePasswordDTO changePasswordDTO,
                                                       @AuthenticationPrincipal User user) {
        return passwordService.changePasswordOfUser(changePasswordDTO, user);
    }
}
