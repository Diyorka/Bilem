package kg.bilem.repository;

import kg.bilem.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    boolean existsByNameAndCategoryId(String name, Long categoryId);

    List<Subcategory> findAllByCategoryId(Long id);
}
