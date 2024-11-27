package thebook.fshop.DTO.Response;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.ShipInfo;
import thebook.fshop.entity.Voucher;
import thebook.fshop.helper.PaymentMethod;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    int ID;
    Date date;
    PaymentMethod paymentMethod;
    PaymentStatus paymentStatus;
    ShipStatus shipStatus;
    long totalAmount;
    long totalAmountBefore;
    Book firstBook;
    int totalBook;

}
