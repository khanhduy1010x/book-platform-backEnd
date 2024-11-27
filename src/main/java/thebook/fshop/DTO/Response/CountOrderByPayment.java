package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.PaymentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountOrderByPayment {
    PaymentStatus paymentStatus;
    long count;
}
