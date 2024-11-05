package thebook.fshop.DTO.Request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.validation.PasswordValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePasswordRequest {
    @NotBlank(message = "NULL_PASSWORD")
    @PasswordValidation
    String password;
}
