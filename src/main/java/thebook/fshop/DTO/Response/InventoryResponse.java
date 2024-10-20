package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResponse {
    Integer inventoryID;
    Integer bookID;
    String bookTitle;  // Đảm bảo `Book` có trường `bookName`
    int quantity;
}
