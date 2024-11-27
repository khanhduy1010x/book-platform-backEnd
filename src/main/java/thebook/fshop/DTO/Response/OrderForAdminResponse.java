package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Order;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderForAdminResponse {
    List<Order> listOrder;
    int totalPages;
    int currentPage;
    List<CountOrderByShip> countOrderByShip;
    List<CountOrderByPayment> countOrderByPayment;
}
