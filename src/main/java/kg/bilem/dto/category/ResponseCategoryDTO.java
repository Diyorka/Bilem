package kg.bilem.dto.category;

import kg.bilem.dto.subcategory.ResponseSubcategoryDTO;
import kg.bilem.model.Category;
import kg.bilem.model.Subcategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

import static kg.bilem.dto.subcategory.ResponseSubcategoryDTO.toResponseSubcategoryDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCategoryDTO {
    Long id;

    String name;

    int coursesCount;

    public static ResponseCategoryDTO toResponseCategoryDTO(Category category){
        return ResponseCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .coursesCount(category.getCoursesCount())
                .build();
    }

    public static List<ResponseCategoryDTO> toResponseCategoryDTO(List<Category> categories){
        return categories.stream().map(ResponseCategoryDTO::toResponseCategoryDTO).collect(Collectors.toList());
    }
}
