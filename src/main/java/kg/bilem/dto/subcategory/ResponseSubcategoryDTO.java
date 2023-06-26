package kg.bilem.dto.subcategory;

import kg.bilem.dto.category.ResponseCategoryDTO;
import kg.bilem.model.Category;
import kg.bilem.model.Subcategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseSubcategoryDTO {
    Long id;

    String name;

    String category;

    public static ResponseSubcategoryDTO toResponseSubcategoryDTO(Subcategory subcategory){
        return ResponseSubcategoryDTO.builder()
                .id(subcategory.getId())
                .name(subcategory.getName())
                .category(subcategory.getCategory().getName())
                .build();
    }

    public static List<ResponseSubcategoryDTO> toResponseSubcategoryDTO(List<Subcategory> subcategories){
        return subcategories.stream().map(ResponseSubcategoryDTO::toResponseSubcategoryDTO).collect(Collectors.toList());
    }
}
