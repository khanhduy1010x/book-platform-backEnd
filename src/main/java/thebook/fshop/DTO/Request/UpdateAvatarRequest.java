package thebook.fshop.DTO.Request;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.validation.FileSizeValidation;
import thebook.fshop.validation.FileValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAvatarRequest {

    MultipartFile file;
}
