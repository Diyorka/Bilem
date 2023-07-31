package kg.bilem.dto.favorite;

import kg.bilem.dto.course.ResponseMainCourseDTO;
import kg.bilem.model.Favorite;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

import static kg.bilem.dto.course.ResponseMainCourseDTO.toResponseMainCourseDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseFavoriteDTO {
    Long id;

    ResponseMainCourseDTO courseDTO;

    public static ResponseFavoriteDTO toResponseFavoriteDTO(Favorite favorite){
        return ResponseFavoriteDTO.builder()
                .id(favorite.getId())
                .courseDTO(toResponseMainCourseDTO(favorite.getCourse()))
                .build();
    }

    public static List<ResponseFavoriteDTO> toResponseFavoriteDTO(List<Favorite> favorites){
        return favorites.stream().map(ResponseFavoriteDTO::toResponseFavoriteDTO).collect(Collectors.toList());
    }
}
