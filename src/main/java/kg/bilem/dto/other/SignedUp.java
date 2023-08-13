package kg.bilem.dto.other;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Author: Diyor Umurzakov
 * GitHub: Diyorka
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignedUp {
    Boolean isSignedUp;
}
