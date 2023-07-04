package kg.bilem.model;

import jakarta.persistence.*;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails {
    String name;

    String email;

    String password;

    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    Role role;

    String imageUrl;

    @Column(length = 500)
    String about_me;

    @Column(length = 500)
    String profile_description;

    String activitySphere;

    @ManyToOne
    City city;

    String workPlace;

    String instagram;

    String github;

    String behance;

    String twitter;

    String youtube;

    String telegram;

    String dribble;

    @ManyToMany
    @JoinTable(
            name = "teacher_course",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    List<Course> teachingCourses;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    List<Course> studyingCourses;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return status == Status.ACTIVE;
    }
}
