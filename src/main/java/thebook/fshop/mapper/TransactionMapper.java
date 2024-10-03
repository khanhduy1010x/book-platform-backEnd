package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thebook.fshop.DTO.Response.TransactionResponse;
import thebook.fshop.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "account", target = "account")
    TransactionResponse toTransactionResponse(Transaction transaction);
}
