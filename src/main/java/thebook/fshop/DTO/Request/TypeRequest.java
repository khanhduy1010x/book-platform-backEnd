package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import thebook.fshop.helper.Role;
import thebook.fshop.validation.EnumValue;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeRequest {
    @EnumValue(enumClass = Role.class ,name = "role")
    String role;
}
