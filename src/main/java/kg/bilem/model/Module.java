package kg.bilem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "module")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Module extends BaseEntity{
    String title;

    @ManyToOne
    Course course;
}
