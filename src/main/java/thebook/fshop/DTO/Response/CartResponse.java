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
    int accountID; // Account ID from the Account entity
    String accountUsername; // Optional: Username from the Account entity
    String accountFullName; // Full name from the Account entity
    MemberType accountMemberType; // Member type from the Account entity
    int bookID; // Book ID from the Book entity
    String bookName; // Book name from the Book entity
    String bookAuthor; // Author from the Book entity
    long bookPrice; // Price from the Book entity
    MemberType bookMemberType; // Member type from the Book entity
    String bookCoverImage; // Cover image URL from the Book entity
    int quantity; // Quantity from the Cart entity
}
