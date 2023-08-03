package kg.bilem.repository;

import kg.bilem.model.StudentProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {
    Optional<StudentProgress> findByLessonIdAndStudentId(Long lessonId, Long id);
}
