package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.CartItem;
import thebook.fshop.entity.Voucher;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    int cartID;
    List<CartItemResponse> cartItems;
    List<Voucher> appliedVoucher;
    long totalPriceAfterSale;
    long totalSale;
    Account account;
}
