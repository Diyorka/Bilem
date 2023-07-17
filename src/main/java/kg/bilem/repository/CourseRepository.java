package kg.bilem.repository;

import kg.bilem.enums.CourseType;
import kg.bilem.enums.Language;
import kg.bilem.enums.Status;
import kg.bilem.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAllBySubcategoryId(Long id, Pageable pageable);

    boolean existsByTitle(String title);

    Course findByTitle(String titleOfCourse);

    Page<Course> findAllByStatus(Status status, Pageable pageable);

    Page<Course> findAllByStatusAndCourseTypeOrderByCreatedAtDesc(Status status, CourseType courseType, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.status = :status AND c.courseType = :courseType ORDER BY SIZE(c.students) DESC")
    Page<Course> findAllByStatusAndCourseTypeOrderByNumberOfStudentsDesc(Status status, CourseType courseType, Pageable pageable);

    Page<Course> findAllByStatusAndTitleContainsIgnoreCase(Status status, String query, Pageable pageable);

    Page<Course> findAllByStatusAndTitleContainsIgnoreCaseAndLanguageAndCourseType(Status active, String query, Language lang, CourseType ct, Pageable pageable);

    Page<Course> findAllByStatusAndCourseTypeAndLanguage(Status active, CourseType paid, Language lang, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.status = :status ORDER BY SIZE(c.students) DESC")
    Page<Course> findTopCourses(Status status, Pageable pageable);

}
