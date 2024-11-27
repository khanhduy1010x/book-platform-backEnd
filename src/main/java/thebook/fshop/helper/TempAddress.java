package thebook.fshop.helper;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TempAddress {
    String province;
    String district;
    String wards;
    String shipDetail;
    String name;
    String phone;
    String paymentMethod;
}
