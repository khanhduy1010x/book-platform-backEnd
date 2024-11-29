package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateBookRequest {
    String id;
    String cateName;
    String bookName;
    String author;
    String price;
    String memberType;
    MultipartFile url;
    MultipartFile coverImage;
    String description;
    String bookType;
    Boolean isVisible;

}
