package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.MembershipPackage;
import thebook.fshop.entity.Order;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberPackageForAdminResponse {
        List<MembershipPackage> membershipPackages;
        int totalPages;
        int currentPage;
        List<CountMemberByType> listMemberByType;

}
