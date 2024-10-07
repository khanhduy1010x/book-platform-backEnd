package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
import thebook.fshop.entity.BookReadHistory;

@Mapper(componentModel = "spring")
public interface BookReadHistoryMapper {
        @Mapping(source = "account" , target = "account")
    BookReadHistoryResponse toBookReadHistoryResponse(BookReadHistory bookReadHistory);
}