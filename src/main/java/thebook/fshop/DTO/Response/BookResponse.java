package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Category;
import thebook.fshop.helper.MemberType;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
    int ID;
    Category category;
    String bookName;
    String author;
    long price;
    MemberType memberType;
    String url;
    String coverImage;
    String description;
}
