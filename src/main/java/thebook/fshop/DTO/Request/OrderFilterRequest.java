package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.PaymentMethod;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderFilterRequest {
    Integer orderID;                    // ID của đơn hàng (optional)
    Integer accountID;                  // ID của tài khoản đặt đơn hàng (optional)
    PaymentStatus paymentStatus;
    PaymentMethod paymentMethod;
    ShipStatus shipStatus;
    Long totalAmount;                   // Tổng số tiền đơn hàng (optional)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date toDate;
}
