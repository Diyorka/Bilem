package kg.bilem.service;

import kg.bilem.dto.favorite.RequestFavoriteDTO;
import kg.bilem.dto.favorite.ResponseFavoriteDTO;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface FavoriteService {
    Page<ResponseFavoriteDTO> getAllFavoritesOfUser(Pageable pageable, User user);
    ResponseEntity<ResponseWithMessage> addFavorite(RequestFavoriteDTO requestFavoriteDTO, User user);
    ResponseEntity<ResponseWithMessage> deleteFavoriteById(Long id, User user);
}
