package thebook.fshop.DTO.Request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.Role;
import thebook.fshop.validation.EnumValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberRoleUpRequest {
    int accID;

    @NotBlank(message = "INVALID_ROLE")
    @EnumValue(name = "role", enumClass = Role.class)
    String role;
}
