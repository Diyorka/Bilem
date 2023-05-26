package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.city.ResponseCityDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.dto.user.UpdateUserDTO;
import kg.bilem.model.User;
import kg.bilem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с пользователями",
        description = "В этом контроллере есть возможность получения, добавления, изменения и удаления пользователей"
)
public class UserController {
    private final UserService userService;
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

    @SecurityRequirement(name = "JWT")
    @PutMapping("/change-my-info")
    @Operation(
            summary = "Изменение данных авторизованного пользователя"
    )
    public GetUserDTO changeUserInfo(@RequestBody @Valid UpdateUserDTO userDto,
                                     @AuthenticationPrincipal User user){
        return userService.changeUserInfo(userDto, user);
    }

    @PostMapping("/addAdmin")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Добавление нового администратора"
    )
    public ResponseEntity<String> addAdmin(@RequestBody CreateUserDTO userDto){
        return userService.addAdmin(userDto);
    }

}
