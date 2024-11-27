package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.CountAccountByRole;
import thebook.fshop.entity.Order;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListAccountForAdminResponse {
    List<Account> accounts;
    int totalPages;
    int currentPage;
    List<CountAccountByMemberType> countOrderByPayment;
    List<CountAccountByRole> countOrderByRole;
    List<CountByActiveStatus> listCountByActiveStatus;
}
