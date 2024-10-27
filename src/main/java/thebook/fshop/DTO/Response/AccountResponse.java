package thebook.fshop.DTO.Response;

import java.util.Date;
import java.util.List;

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
    long totalElements;      // Total number of elements
    int totalPages;         // Total number of pages
    int number;             // Current page number

    // Existing account fields
    int accID;
    Role role;
    String phone;
    String fullName;
    Long amount;
    MemberType memberType;
    String address;
    String avatar;
    Date birth;
    String username;

    boolean hasPassword;
    boolean skip_password_prompt;
}
