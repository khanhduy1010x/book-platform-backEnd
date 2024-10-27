package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Account;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListStatisticsByMembershipPackageResponse {
    int accId;
    String packageName;
    BigDecimal total;
}
