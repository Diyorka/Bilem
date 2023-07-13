package kg.bilem.model;

import jakarta.persistence.*;
import kg.bilem.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "review")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review extends BaseEntity{
    String text;

    int score;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER)
    Set<ReviewReply> reviewReplies;

    @ManyToOne
    Course course;

    @ManyToOne
    User user;

    @Enumerated(EnumType.STRING)
    Status status;
}
