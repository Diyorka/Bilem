package kg.bilem.service;

import kg.bilem.dto.category.RequestCategoryDTO;
import kg.bilem.dto.category.ResponseCategoryDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    List<ResponseCategoryDTO> getAllCategories();

    List<ResponseCategoryDTO> getTop8CategoriesByCourseCount();

    ResponseCategoryDTO getCategoryById(Long id);

    ResponseEntity<String> addCategory(RequestCategoryDTO categoryDTO);

}
