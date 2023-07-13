package kg.bilem.repository;

import kg.bilem.enums.Status;
import kg.bilem.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAllBySubcategoryId(Long id, Pageable pageable);
    boolean existsByTitle(String title);
    Course findByTitle(String titleOfCourse);

    Page<Course> findAllByStatus(Status status, Pageable pageable);
}
