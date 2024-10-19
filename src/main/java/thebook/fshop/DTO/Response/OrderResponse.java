package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Book;
import thebook.fshop.helper.PaymentMethod;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    int orderID;                        // Order ID
    int accountID;                      // Account ID who placed the order
    List<BookItem> orderedBooks;        // List of ordered books and their quantities
    Date date;                          // Date of the order
    PaymentMethod paymentMethod;        // Payment method
    PaymentStatus paymentStatus;        // Status of payment
    ShipStatus shipStatus;              // Shipping status
    long totalAmount;                   // Total order amount

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class BookItem {
        Book book;                     // Book details
        int quantity;                  // Quantity of the book
    }
}
