package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.BookRate;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDetailResponse {
    Book book;
    List<BookRate> bookRates;
    BookRate bookRateOfUser;
    boolean isOwned;
    double readPercent;
}
