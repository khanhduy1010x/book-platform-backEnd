package thebook.fshop.DTO.Response;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    boolean authenticated;
    Date expiryTime;
    Date refreshedTime;
}
