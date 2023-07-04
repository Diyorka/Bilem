package kg.bilem.repository;

import kg.bilem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByTitle(String title);
    Course findByTitle(String titleOfCourse);
}
