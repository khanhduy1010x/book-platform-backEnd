package thebook.fshop.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import thebook.fshop.DTO.Request.AccountCreationRequest;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.DTO.Response.ForgotPasswordResponse;
import thebook.fshop.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "username", source = "username")
    Account toAccount(AccountCreationRequest request);

    @Mapping(target = "skip_password_prompt", source = "skip_password_prompt")
    AccountResponse toAccountResponse(Account account);

    ForgotPasswordResponse toForgotPasswordResponse(Account account);
}
