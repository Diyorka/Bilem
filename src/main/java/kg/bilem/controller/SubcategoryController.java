package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.subcategory.RequestSubcategoryDTO;
import kg.bilem.dto.subcategory.ResponseSubcategoryDTO;
import kg.bilem.service.impls.SubcategoryServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategory")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с подкатегориями",
        description = "В этом контроллере есть возможность получения, добавления и изменения подкатегорий"
)
public class SubcategoryController {
    SubcategoryServiceImpl subcategoryService;

    @GetMapping("/all")
    @Operation(
            summary = "Получение всех подкатегорий"
    )
    public List<ResponseSubcategoryDTO> getAllSubcategories() {
        return subcategoryService.getAllSubcategories();
    }

    @GetMapping("/all/byCategory/{id}")
    @Operation(
            summary = "Получение всех подкатегорий по айди категории"
    )
    public List<ResponseSubcategoryDTO> getAllSubcategoriesByCategoryId(@PathVariable Long id) {
        return subcategoryService.getAllSubcategoriesByCategoryId(id);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение подкатегории по айди"
    )
    public ResponseSubcategoryDTO getSubcategoryById(@PathVariable Long id) {
        return subcategoryService.getSubcategoryById(id);
    }

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Добавление новой подкатегории"
    )
    public ResponseEntity<String> addSubcategory(@RequestBody RequestSubcategoryDTO subcategoryDTO) {
        return subcategoryService.addSubcategory(subcategoryDTO);
    }
}
