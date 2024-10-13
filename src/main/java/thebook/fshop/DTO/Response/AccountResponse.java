package thebook.fshop.DTO.Response;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.MemberType;
import thebook.fshop.helper.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    int accID;
    Role role;
    String phone;
    String fullName;
    Long amount;
    MemberType memberType;
    String address;
    String avatar;
    Date birth;
    boolean hasPassword;
}
