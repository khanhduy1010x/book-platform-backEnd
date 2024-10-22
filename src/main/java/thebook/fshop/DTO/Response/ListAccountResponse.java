package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListAccountResponse {
    int accID;
    String phone;
    String fullName;
    String email;
    String avatar;
    boolean isBanned;
    String username;
    String role;
}
