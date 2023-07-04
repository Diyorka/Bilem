package kg.bilem.dto.user;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDTO {
    private String email;
    private String password;
}
