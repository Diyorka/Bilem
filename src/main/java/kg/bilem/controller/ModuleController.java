package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.course.RequestCourseDTO;
import kg.bilem.dto.course.ResponseCourseDTO;
import kg.bilem.dto.module.RequestModuleDTO;
import kg.bilem.dto.module.ResponseModuleDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.ModuleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/module")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с модулями",
        description = "В этом контроллере есть возможность получения, добавления и изменения модулей"
)
public class ModuleController {
    private final ModuleServiceImpl moduleService;

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Создание модуля"
    )
    public ResponseModuleDTO createModule(@RequestBody RequestModuleDTO moduleDTO,
                                          @AuthenticationPrincipal User user) {
        return moduleService.createModule(moduleDTO, user);
    }

}
