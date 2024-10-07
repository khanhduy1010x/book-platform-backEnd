package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Book;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookReadHistoryResponse {
    int id;
    Book book;
    int accID;
}
