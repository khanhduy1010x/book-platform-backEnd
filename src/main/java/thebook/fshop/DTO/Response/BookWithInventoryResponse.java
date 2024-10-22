package thebook.fshop.DTO.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookWithInventoryResponse {
    private int bookId;
    private String bookName;
    private String author;
    private double price;
    private int quantityInStock;
}
