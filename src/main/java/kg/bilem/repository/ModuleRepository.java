package kg.bilem.repository;

import kg.bilem.model.Course;
import kg.bilem.model.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    boolean existsByTitleAndCourseId(String title, Long courseId);

    Page<Module> findAllByCourseId(Long courseId, Pageable pageable);

    boolean existsByCourseId(Long courseId);

    boolean existsByOrdinalNumberAndCourseId(Long ordinalNumber, Long id);

    List<Module> findAllByCourse(Course course);
}
