package thebook.fshop.DTO.Response;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Book;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListBookByCateResponse {
    String cateName;
    List<Book> listBook;
}
