package kg.bilem.model;

import jakarta.persistence.*;
import kg.bilem.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "notification")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends BaseEntity{
    String header;

    String message;

    @ManyToOne
    User user;

    @Enumerated(EnumType.STRING)
    Status status;

    @Column(columnDefinition = "boolean default false")
    boolean read;
}
