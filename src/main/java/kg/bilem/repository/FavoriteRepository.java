package kg.bilem.repository;

import kg.bilem.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByCourseIdAndUserId(Long courseId, Long id);
}
