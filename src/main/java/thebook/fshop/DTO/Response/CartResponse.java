package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.MemberType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    int cartID;
    int accountID;
    String accountUsername;
    MemberType accountMemberType;
    int bookID;
    String bookName;
    String bookAuthor;
    long bookPrice;
    MemberType bookMemberType;
    String bookCoverImage;
    int quantity;
}
