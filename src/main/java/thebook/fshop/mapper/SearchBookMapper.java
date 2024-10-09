package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thebook.fshop.DTO.Response.BookResponse;
import thebook.fshop.entity.Book;

@Mapper(componentModel = "spring")
public interface SearchBookMapper {
    BookResponse toBookResponse(Book book);
}
