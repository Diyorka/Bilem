package kg.bilem.dto.user;

import jakarta.persistence.Column;
import kg.bilem.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetUserDTO {
    Long id;
    String name;

    String email;

    String imageUrl;

    String about_me;

    String profile_description;

    String activity_sphere;

    String city;

    String work_place;

    String instagram;

    String github;

    String behance;

    String twitter;

    String youtube;

    String telegram;

    String dribble;

    String role;

    public static GetUserDTO toGetUserDto(User user){
        return GetUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .imageUrl(user.getImage_url())
                .about_me(user.getAbout_me())
                .profile_description(user.getProfile_description())
                .activity_sphere(user.getActivity_sphere())
                .city(user.getCity() == null ? null : user.getCity().getName())
                .work_place(user.getWork_place())
                .instagram(user.getInstagram())
                .github(user.getGithub())
                .behance(user.getBehance())
                .twitter(user.getTwitter())
                .youtube(user.getYoutube())
                .telegram(user.getTelegram())
                .dribble(user.getDribble())
                .role(user.getRole().name())
                .build();
    }

    public static List<GetUserDTO> toGetUserDto(List<User> users){
        return users.stream().map(GetUserDTO::toGetUserDto).collect(Collectors.toList());
    }

}
