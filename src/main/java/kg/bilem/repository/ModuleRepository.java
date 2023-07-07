package kg.bilem.repository;

import kg.bilem.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    boolean existsByTitleAndCourseId(String title, Long courseId);
}
