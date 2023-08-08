package kg.bilem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "subcategory")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subcategory extends BaseEntity{
    String name;

    @ManyToOne
    Category category;

    @OneToMany(mappedBy = "subcategory", fetch = FetchType.EAGER)
    List<Course> courses;
}
