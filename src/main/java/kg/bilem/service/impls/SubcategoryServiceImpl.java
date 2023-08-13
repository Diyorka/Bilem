package kg.bilem.service.impls;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.subcategory.RequestSubcategoryDTO;
import kg.bilem.dto.subcategory.ResponseSubcategoryDTO;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Category;
import kg.bilem.model.Subcategory;
import kg.bilem.repository.CategoryRepository;
import kg.bilem.repository.SubcategoryRepository;
import kg.bilem.service.SubcategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static kg.bilem.dto.subcategory.ResponseSubcategoryDTO.toResponseSubcategoryDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {
    SubcategoryRepository subcategoryRepository;
    CategoryRepository categoryRepository;

    @Override
    public List<ResponseSubcategoryDTO> getAllSubcategories() {
        return toResponseSubcategoryDTO(subcategoryRepository.findAll());
    }

    @Override
    public List<ResponseSubcategoryDTO> getAllSubcategoriesByCategoryId(Long id){
        return toResponseSubcategoryDTO(subcategoryRepository.findAllByCategoryId(id));
    }

    @Override
    public ResponseSubcategoryDTO getSubcategoryById(Long id) {
        return toResponseSubcategoryDTO(subcategoryRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Подкатегория с айди " + id + " не найдена")
            )
        );
    }

    @Override
    public ResponseEntity<ResponseWithMessage> addSubcategory(RequestSubcategoryDTO subcategoryDTO) {
        if(subcategoryRepository.existsByNameAndCategoryId(subcategoryDTO.getName(), subcategoryDTO.getCategoryId())){
            throw new AlreadyExistException("Подкатегория с таким названием уже существует в данной категории");
        }

        Subcategory subcategory = new Subcategory();
        subcategory.setName(subcategoryDTO.getName());
        Category category = categoryRepository.findById(subcategoryDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Категория с айди " + subcategoryDTO.getCategoryId()
                        + " не найдена"));
        subcategory.setCategory(category);
        subcategoryRepository.save(subcategory);

        return ResponseEntity.ok(new ResponseWithMessage("Подкатегория добавлена"));
    }
}
