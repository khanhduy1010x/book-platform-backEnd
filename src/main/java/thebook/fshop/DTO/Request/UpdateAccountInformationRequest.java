package thebook.fshop.DTO.Request;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.validation.FullNameValidation;

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
