package thebook.fshop.DTO.Response;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.*;
import thebook.fshop.helper.PaymentMethod;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    int ID;
    Date date;
    PaymentMethod paymentMethod;
    PaymentStatus paymentStatus;
    ShipStatus shipStatus;
    long totalAmount;
    long totalAmountBefore;
    ShipInfo shipInfo;
    Set<Voucher> vouchers;
   List<BookInDetailResponse> listBook;
   Order order;
}
