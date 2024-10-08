package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import thebook.fshop.DTO.Response.BookRateResponse;
import thebook.fshop.entity.BookRate;

@Mapper(componentModel = "spring")
public interface BookRateMapper {
    BookRateResponse toBookRateResponse(BookRate bookRate);
}
