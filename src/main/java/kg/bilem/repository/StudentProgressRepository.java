package kg.bilem.repository;

import kg.bilem.model.StudentProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {
    Optional<StudentProgress> findByLessonIdAndStudentId(Long lessonId, Long id);

    @Query("SELECT COUNT(sp) FROM StudentProgress sp WHERE sp.completed = true AND sp.student.id = :studentId AND sp.lesson.module.course.id = :courseId")
    int getCompletedLessonsCountForStudentInCourse(Long studentId, Long courseId);
}
