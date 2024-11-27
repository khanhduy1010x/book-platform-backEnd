package thebook.fshop.helper;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.DTO.Request.OrderCreationRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentInfo {
    private double price;
    private boolean status;
    private int accID;
    private OrderCreationRequest tempAddress;
}
