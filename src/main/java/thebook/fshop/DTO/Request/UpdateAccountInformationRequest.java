package thebook.fshop.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.validation.FullNameValidation;
import thebook.fshop.validation.NumberPhoneValidation;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAccountInformationRequest {

    @NotBlank(message = "NULL_USERNAME")
    @FullNameValidation
    String name;
    Date birth;
}
