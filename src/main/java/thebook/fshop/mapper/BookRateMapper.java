package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thebook.fshop.DTO.Response.BookRateResponse;
import thebook.fshop.entity.BookRate;

@Mapper(componentModel = "spring")
public interface BookRateMapper {
    @Mapping(source = "book.ID", target = "bookID")
    BookRateResponse toBookRateResponse(BookRate bookRate);
}
