package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Category;
import thebook.fshop.helper.MemberType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddtoCartRequest {
    int bookID;
    int quantity;
}
