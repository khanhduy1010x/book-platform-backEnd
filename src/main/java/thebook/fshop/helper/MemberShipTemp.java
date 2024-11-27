package thebook.fshop.helper;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.MembershipPackage;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberShipTemp {
    MembershipPackage membershipPackage;
    Account account;
    boolean status;
    long price;
}
