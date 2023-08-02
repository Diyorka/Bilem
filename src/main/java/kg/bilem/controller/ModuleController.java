package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.course.RequestCourseDTO;
import kg.bilem.dto.course.ResponseCourseDTO;
import kg.bilem.dto.module.RequestModuleDTO;
import kg.bilem.dto.module.ResponseModuleDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.ModuleServiceImpl;
import kg.bilem.service.impls.VideoServiceImpl;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/module")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с модулями",
        description = "В этом контроллере есть возможность получения, добавления и изменения модулей"
)
public class ModuleController {
    private final ModuleServiceImpl moduleService;

    @GetMapping("/{course_id}/all")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение всех модулей по айди курса"
    )
    public Page<ResponseModuleDTO> getModulesByCourseId(@PathVariable Long course_id,
                                                        @PageableDefault Pageable pageable,
                                                        @AuthenticationPrincipal User user){
        return moduleService.getModulesByCourseId(course_id, pageable, user);
    }

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Создание модуля"
    )
    public ResponseModuleDTO createModule(@RequestBody @Valid RequestModuleDTO moduleDTO,
                                          @AuthenticationPrincipal User user) {
        return moduleService.createModule(moduleDTO, user);
    }

    @PutMapping("/{module_id}/edit")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Редактирование модуля"
    )
    public ResponseEntity<String> editModule(@PathVariable Long module_id,
                                             @RequestBody RequestModuleDTO moduleDTO,
                                             @AuthenticationPrincipal User user){
        return moduleService.editModule(module_id, moduleDTO, user);
    }

    @DeleteMapping("/{module_id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление модуля и всех его уроков"
    )
    public ResponseEntity<String> deleteModule(@PathVariable Long module_id,
                                               @AuthenticationPrincipal User user){
        return moduleService.deleteModule(module_id, user);
    }

//    @PostMapping(value = "/add-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Operation(
//            summary = "Добавление видео"
//    )
//    public ResponseEntity<String> uploadVideo(@RequestPart MultipartFile file) throws IOException {
//        return videoService.saveVideoForLesson(file);
//    }

}
