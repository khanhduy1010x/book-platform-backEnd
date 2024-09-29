package thebook.fshop.mapper;

import org.mapstruct.Mapper;

import thebook.fshop.DTO.Request.AccountCreationRequest;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AccountCreationRequest request);

    AccountResponse toAccountResponse(Account account);
}
