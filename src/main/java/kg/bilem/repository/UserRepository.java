package kg.bilem.repository;

import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findAllByStatus(Status active, Pageable pageable);

    Page<User> findAllByStatusAndRole(Status active, Role role, Pageable pageable);

    List<User> findByNameContainsIgnoreCaseAndStatusAndRole(String name, Status active, Role teacher);
}
