package kg.bilem.service.impls;

import kg.bilem.dto.favorite.RequestFavoriteDTO;
import kg.bilem.dto.favorite.ResponseFavoriteDTO;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Favorite;
import kg.bilem.model.User;
import kg.bilem.repository.CourseRepository;
import kg.bilem.repository.FavoriteRepository;
import kg.bilem.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static kg.bilem.dto.favorite.ResponseFavoriteDTO.toResponseFavoriteDTO;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final CourseRepository courseRepository;

    @Override
    public Page<ResponseFavoriteDTO> getAllFavoritesOfUser(Pageable pageable, User user) {
        Page<Favorite> favorites = favoriteRepository.findAll(pageable);
        List<ResponseFavoriteDTO> favoriteDTOS = toResponseFavoriteDTO(favorites.toList());
        return new PageImpl<>(favoriteDTOS, pageable, favorites.getTotalElements());
    }

    @Override
    public ResponseEntity<String> addFavorite(RequestFavoriteDTO requestFavoriteDTO, User user) {
        if(favoriteRepository.existsByCourseIdAndUserId(requestFavoriteDTO.getCourse_id(), user.getId())){
            throw new AlreadyExistException("Курс уже находится в избранных");
        }

        favoriteRepository.save(Favorite.builder()
                .course(courseRepository.findById(requestFavoriteDTO.getCourse_id())
                        .orElseThrow(() -> new NotFoundException("Курс с таким айди не найден"))
                )
                .user(user)
                .build()
        );
        return  ResponseEntity.ok("Курс успешно добавлен в избранное");
    }

    @Override
    public ResponseEntity<String> deleteFavoriteById(Long id, User user) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Избранное не найдено"));
        if(!favorite.getUser().equals(user)){
            throw new NoAccessException("У вас нет доступа к данному избранному");
        }

        favoriteRepository.delete(favorite);
        return ResponseEntity.ok("Успешно удалено из избранных");
    }
}
