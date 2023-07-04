package kg.bilem.dto.category;

import kg.bilem.model.Category;
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
public class ResponseCategoryDTO {
    Long id;

    String name;

    public static ResponseCategoryDTO toResponseCategoryDTO(Category category){
        return ResponseCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<ResponseCategoryDTO> toResponseCategoryDTO(List<Category> categories){
        return categories.stream().map(ResponseCategoryDTO::toResponseCategoryDTO).collect(Collectors.toList());
    }
}
