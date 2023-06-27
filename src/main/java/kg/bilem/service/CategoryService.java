package kg.bilem.service;

import kg.bilem.dto.category.RequestCategoryDTO;
import kg.bilem.dto.category.ResponseCategoryDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    List<ResponseCategoryDTO> getAllCategories();

    ResponseCategoryDTO getCategoryById(Long id);

    ResponseEntity<String> addCategory(RequestCategoryDTO categoryDTO);
}
