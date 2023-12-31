package kg.bilem.service.impls;

import kg.bilem.dto.category.RequestCategoryDTO;
import kg.bilem.dto.category.ResponseCategoryDTO;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Category;
import kg.bilem.repository.CategoryRepository;
import kg.bilem.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static kg.bilem.dto.category.ResponseCategoryDTO.toResponseCategoryDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Override
    public List<ResponseCategoryDTO> getAllCategories() {
        return toResponseCategoryDTO(categoryRepository.findAll());
    }

    @Override
    public List<ResponseCategoryDTO> getTop8CategoriesByCourseCount() {
        return toResponseCategoryDTO(categoryRepository.findTop8ByOrderByCoursesCountDesc());
    }

    @Override
    public ResponseCategoryDTO getCategoryById(Long id) {
        return toResponseCategoryDTO(categoryRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Категория с айди " + id + " не найдена")
                )
        );
    }

    @Override
    public ResponseEntity<String> addCategory(RequestCategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new AlreadyExistException("Категория с названием " + categoryDTO.getName() + " существует");
        }
        categoryRepository.save(new Category(categoryDTO.getName(), 0));

        return ResponseEntity.ok("Категория успешно добавлена");
    }
}
