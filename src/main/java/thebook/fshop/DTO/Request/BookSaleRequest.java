package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSaleRequest {
    int bookId;
    double salePrice;
    int quantity;
    String isActive;

}
