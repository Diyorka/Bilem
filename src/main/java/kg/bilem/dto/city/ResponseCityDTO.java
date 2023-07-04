package kg.bilem.dto.city;

import kg.bilem.model.City;
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
public class ResponseCityDTO {
    Long id;

    String name;

    public static ResponseCityDTO toResponseCityDTO(City city) {
        return ResponseCityDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    public static List<ResponseCityDTO> toResponseCityDTO(List<City> cities) {
        return cities.stream().map(ResponseCityDTO::toResponseCityDTO).collect(Collectors.toList());
    }
}
