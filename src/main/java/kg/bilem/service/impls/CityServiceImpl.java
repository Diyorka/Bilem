package kg.bilem.service.impls;

import kg.bilem.dto.city.RequestCityDTO;
import kg.bilem.dto.city.ResponseCityDTO;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.City;
import kg.bilem.repository.CityRepository;
import kg.bilem.service.CityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static kg.bilem.dto.city.ResponseCityDTO.toResponseCityDTO;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    CityRepository cityRepository;

    @Override
    public List<ResponseCityDTO> getAllCities() {
        return toResponseCityDTO(cityRepository.findAll());
    }

    @Override
    public ResponseCityDTO getCityById(Long id) {
        return toResponseCityDTO(cityRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Город с айди " + id + " не найден")
                )
        );
    }

    @Override
    public ResponseEntity<String> addCity(RequestCityDTO cityDTO) {
        if (cityRepository.existsByName(cityDTO.getName())) {
            throw new AlreadyExistException("Город с таким названием уже сушествует");
        }
        cityRepository.save(new City(cityDTO.getName()));

        return ResponseEntity.ok("Город успешно добавлен");
    }

    @Override
    public ResponseCityDTO updateCity(Long id, RequestCityDTO cityDTO) {
        City city = cityRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Город с айди " + id + " не найден")
                );
        city.setName(cityDTO.getName());
        cityRepository.save(city);

        return toResponseCityDTO(city);
    }
}
