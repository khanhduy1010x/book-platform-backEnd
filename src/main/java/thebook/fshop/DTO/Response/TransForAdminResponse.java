package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Order;
import thebook.fshop.entity.Transaction;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransForAdminResponse {
    List<Transaction> listTrans;
    int totalPages;
    int currentPage;
    List<CountTransByTransType> countTransByTransType;
    List<CountTransByMethodType> countTransByMethodTypes;
}
