package kg.bilem.model;

import jakarta.persistence.*;
import kg.bilem.enums.Status;
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

    Long ordinalNumber;

    @ManyToOne
    Course course;
}
