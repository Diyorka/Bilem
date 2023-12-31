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
import kg.bilem.service.impls.UserServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с пользователями",
        description = "В этом контроллере есть возможность получения, добавления, изменения и удаления пользователей"
)
public class UserController {
    UserServiceImpl userService;

    @GetMapping("/my-info")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение данных о пользователе"
    )
    public GetUserDTO getUserInfo(@AuthenticationPrincipal User user){
        return userService.getUserInfo(user);
    }

    @GetMapping("/search-teachers/{name}")
    @Operation(
            summary = "Получение всех преподавателей по имени"
    )
    public List<GetUserDTO> getTeachersByName(@PathVariable String name){
        return userService.getTeachersByName(name);
    }

    @PutMapping("/change-my-info")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Изменение данных авторизованного пользователя"
    )
    public GetUserDTO changeUserInfo(@RequestBody @Valid UpdateUserDTO userDto,
                                     @AuthenticationPrincipal User user) {
        return userService.changeUserInfo(userDto, user);
    }

}
