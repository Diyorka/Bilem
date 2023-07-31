package kg.bilem.repository;

import kg.bilem.model.Mailing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingRepository extends JpaRepository<Mailing, Long> {
    boolean existsByEmail(String email);
}
