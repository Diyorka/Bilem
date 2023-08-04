package kg.bilem.repository;

import kg.bilem.enums.Status;
import kg.bilem.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByCourseIdAndUserId(Long courseId, Long id);

    List<Review> findAllByCourseId(Long id);

    Page<Review> findAllByCourseId(Long courseId, Pageable pageable);
}
