package kg.bilem.repository;

import kg.bilem.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByName(String name);

    Page<Course> findAllBySubcategoryId(Long id, Pageable pageable);
    boolean existsByTitle(String title);
    Course findByTitle(String titleOfCourse);
}
