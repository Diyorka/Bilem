package kg.bilem.service;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.subcategory.RequestSubcategoryDTO;
import kg.bilem.dto.subcategory.ResponseSubcategoryDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubcategoryService {
    List<ResponseSubcategoryDTO> getAllSubcategories();

    List<ResponseSubcategoryDTO> getAllSubcategoriesByCategoryId(Long id);

    ResponseSubcategoryDTO getSubcategoryById(Long id);

    ResponseEntity<ResponseWithMessage> addSubcategory(RequestSubcategoryDTO subcategoryDTO);
}
