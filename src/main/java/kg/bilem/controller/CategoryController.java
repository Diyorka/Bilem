package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.category.RequestCategoryDTO;
import kg.bilem.dto.category.ResponseCategoryDTO;
import kg.bilem.dto.city.ResponseCityDTO;
import kg.bilem.service.impls.CategoryServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с категориями",
        description = "В этом контроллере есть возможность получения, добавления и изменения категорий"
)
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping("/all")
    @Operation(
            summary = "Получение всех категорий"
    )
    public List<ResponseCategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение категории по айди"
    )
    public ResponseCategoryDTO getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Добавление новой категории"
    )
    public ResponseEntity<String> addCategory(@RequestBody RequestCategoryDTO categoryDTO){
        return categoryService.addCategory(categoryDTO);
    }
}
