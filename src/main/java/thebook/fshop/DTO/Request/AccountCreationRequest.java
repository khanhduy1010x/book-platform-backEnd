package thebook.fshop.DTO.Request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.validation.NumberPhoneValidation;
import thebook.fshop.validation.PasswordValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCreationRequest {

    @NotBlank(message = "NULL_PASSWORD")
    @PasswordValidation
    String password;

    @NotBlank(message = "NULL_PHONE_NUMBER")
    @NumberPhoneValidation
    String phone;

    @NotBlank(message = "NULL_FULL_NAME")
    String fullName;

    String otp;

    @NotBlank(message = "NULL_USERNAME")
    String username;
}
