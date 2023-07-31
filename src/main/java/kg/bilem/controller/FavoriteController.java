package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bilem.dto.favorite.RequestFavoriteDTO;
import kg.bilem.dto.favorite.ResponseFavoriteDTO;
import kg.bilem.model.User;
import kg.bilem.service.impls.FavoriteServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с избранными",
        description = "В этом контроллере есть возможность получения, добавления и удаления избранных курсов"
)
public class FavoriteController {
    private final FavoriteServiceImpl favoriteService;

    @PostMapping("/add")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление курса в избранное"
    )
    public ResponseEntity<String> addFavorite(@RequestBody @Valid RequestFavoriteDTO favoriteDTO,
                                              @AuthenticationPrincipal User user){
        return favoriteService.addFavorite(favoriteDTO, user);
    }

    @GetMapping("/all")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение избранных курсов пользователя"
    )
    public Page<ResponseFavoriteDTO> getFavoritesOfUser(@PageableDefault Pageable pageable,
                                                        @AuthenticationPrincipal User user){
        return favoriteService.getAllFavoritesOfUser(pageable, user);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление избранного по айди"
    )
    public ResponseEntity<String> deleteFavoriteById(@PathVariable Long id,
                                                     @AuthenticationPrincipal User user){
        return favoriteService.deleteFavoriteById(id, user);
    }

}

