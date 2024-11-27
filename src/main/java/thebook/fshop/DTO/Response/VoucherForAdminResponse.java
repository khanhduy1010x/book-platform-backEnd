package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Transaction;
import thebook.fshop.entity.Voucher;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherForAdminResponse {
    List<Voucher> listVoucher;
    int totalPages;
    int currentPage;
}
