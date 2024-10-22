package thebook.fshop.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartRequest {
    private int cartId;
    private int bookId;
    private int quantity;
}
