package kg.bilem.repository;

import kg.bilem.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByModuleId(Long moduleId);

    Page<Lesson> findAllByModuleId(Long moduleId, Pageable pageable);

    boolean existsByOrdinalNumberAndModuleId(Long ordinalNumber, Long moduleId);

    boolean existsByTitleAndModuleId(String title, Long moduleId);
}
