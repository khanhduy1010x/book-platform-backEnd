package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import thebook.fshop.DTO.Response.BookRateResponse;
import thebook.fshop.entity.BookRate;

@Mapper(
        componentModel = "spring",
        uses = {AccountMapper.class})
public interface BookRateMapper {
    @Mapping(source = "book.ID", target = "bookID")
    @Mapping(source = "account", target = "accountResponse")
    BookRateResponse toBookRateResponse(BookRate bookRate);
}
