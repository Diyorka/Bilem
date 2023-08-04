package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.city.RequestCityDTO;
import kg.bilem.dto.city.ResponseCityDTO;
import kg.bilem.service.impls.CityServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с городами",
        description = "В этом контроллере есть возможность получения, добавления и изменения городов"
)
public class CityController {
    CityServiceImpl cityService;

    @GetMapping("/all")
    @Operation(
            summary = "Получение всех городов"
    )
    public List<ResponseCityDTO> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение города по айди"
    )
    public ResponseCityDTO getCityById(@PathVariable Long id) {
        return cityService.getCityById(id);
    }

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Добавление города"
    )
    public ResponseEntity<String> addCity(@RequestBody @Valid RequestCityDTO cityDTO) {
        return cityService.addCity(cityDTO);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Обновление города"
    )
    public ResponseCityDTO updateCity(@PathVariable Long id, @RequestBody RequestCityDTO cityDTO) {
        return cityService.updateCity(id, cityDTO);
    }

}
