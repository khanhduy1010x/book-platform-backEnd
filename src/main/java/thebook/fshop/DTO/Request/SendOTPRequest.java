package thebook.fshop.DTO.Request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.validation.NumberPhoneValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendOTPRequest {
    @NotBlank
    @NotBlank(message = "NULL_PHONE_NUMBER")
    @NumberPhoneValidation
    String phone;
}
