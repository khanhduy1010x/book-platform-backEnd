package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.entity.Author;
import thebook.fshop.entity.Book;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageAuthorResponse {
    List<Author> listAuthor;
    int totalPages;
    int currentPage;
}
