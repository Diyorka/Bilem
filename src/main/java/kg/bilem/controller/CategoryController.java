package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.category.RequestCategoryDTO;
import kg.bilem.dto.category.ResponseCategoryDTO;
import kg.bilem.dto.city.ResponseCityDTO;
import kg.bilem.service.impls.CategoryServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с категориями",
        description = "В этом контроллере есть возможность получения, добавления и изменения категорий"
)
public class CategoryController {
    CategoryServiceImpl categoryService;

    @GetMapping("/all")
    @Operation(
            summary = "Получение всех категорий"
    )
    public List<ResponseCategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/top-8-categories")
    @Operation(
            summary = "Получение топ 8 категорий по количеству курсов"
    )
    public List<ResponseCategoryDTO> getTop8CategoriesByCourseCount(){
        return categoryService.getTop8CategoriesByCourseCount();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение категории по айди"
    )
    public ResponseCategoryDTO getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Добавление новой категории"
    )
    public ResponseEntity<String> addCategory(@RequestBody @Valid RequestCategoryDTO categoryDTO) {
        return categoryService.addCategory(categoryDTO);
    }
}
