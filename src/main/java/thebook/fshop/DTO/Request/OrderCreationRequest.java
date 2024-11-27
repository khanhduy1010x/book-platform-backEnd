package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.PaymentMethod;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {
    String province;
    String district;
    String wards;
    String shipDetail;
    String name;
    String phone;
    String paymentMethod;
    Integer accID;
}
