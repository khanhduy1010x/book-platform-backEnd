package thebook.fshop.DTO.Response;


import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Order;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListBookPageResponse {
    List<OrderResponse> listOrder;
    int totalPages;
    int currentPage;

}
