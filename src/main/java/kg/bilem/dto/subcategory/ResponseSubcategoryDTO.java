package kg.bilem.dto.subcategory;

import kg.bilem.dto.category.ResponseCategoryDTO;
import kg.bilem.model.Category;
import kg.bilem.model.Subcategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

import static kg.bilem.dto.category.ResponseCategoryDTO.toResponseCategoryDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseSubcategoryDTO {
    Long id;

    String name;

    ResponseCategoryDTO category;

    public static ResponseSubcategoryDTO toResponseSubcategoryDTO(Subcategory subcategory){
        return ResponseSubcategoryDTO.builder()
                .id(subcategory.getId())
                .name(subcategory.getName())
                .category(toResponseCategoryDTO(subcategory.getCategory()))
                .build();
    }

    public static List<ResponseSubcategoryDTO> toResponseSubcategoryDTO(List<Subcategory> subcategories){
        return subcategories.stream().map(ResponseSubcategoryDTO::toResponseSubcategoryDTO).collect(Collectors.toList());
    }
}
