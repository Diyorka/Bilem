package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.AuthenticationResponse;
import kg.bilem.dto.user.AuthUserDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.exception.UserAlreadyExistException;
import kg.bilem.service.impls.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для авторизации, регистрации, подтверждения аккаунта"
)
public class AuthenticationController {
    private final AuthenticationServiceImpl service;

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового аккаунта"
    )
    public ResponseEntity<String> register(
            @Valid @RequestBody CreateUserDTO request
    ) throws UserAlreadyExistException {
        return service.register(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Авторизация активированного аккаунта"
    )
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthUserDTO request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Обновление токена"
    )
    public ResponseEntity<AuthenticationResponse> refresh(@RequestParam String refreshToken) throws IOException {
        return ResponseEntity.ok(service.refreshToken(refreshToken));
    }

    @GetMapping("/activate/{token}")
    @Operation(
            summary = "Активация аккаунта с помощью кода, отправленного на почту"
    )
    public ResponseEntity<String> activate(@PathVariable String token) {
        return service.activateAccount(token);
    }
}
