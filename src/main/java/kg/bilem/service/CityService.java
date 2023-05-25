package kg.bilem.service;

import kg.bilem.dto.city.RequestCityDTO;
import kg.bilem.dto.city.ResponseCityDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CityService {
    List<ResponseCityDTO> getAllCities();

    ResponseCityDTO getCityById(Long id);

    ResponseEntity<String> addCity(RequestCityDTO cityDTO);

    ResponseCityDTO updateCity(Long id, RequestCityDTO cityDTO);
}
