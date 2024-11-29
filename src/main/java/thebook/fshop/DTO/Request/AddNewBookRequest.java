package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import thebook.fshop.helper.BookType;
import thebook.fshop.helper.EbookType;
import thebook.fshop.helper.MemberType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddNewBookRequest {
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
