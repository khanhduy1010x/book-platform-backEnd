package thebook.fshop.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.*;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.ForgotPasswordResponse;
import thebook.fshop.DTO.Response.ListAccountResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.helper.MemberType;
import thebook.fshop.mapper.AccountMapper;
import thebook.fshop.service.AccountService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountManagementController {
    AccountService accountService;
    AccountMapper accountMapper;

    @GetMapping("/view-profile")
    ApiResponse<AccountResponse> viewProfile() {
        AccountResponse response = accountService.getMyInfo();
        return ApiResponse.<AccountResponse>builder().result(response).build();
    }

    @PostMapping(
            value = "/edit-avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<AccountResponse> editAvatar(@ModelAttribute @Valid UpdateAvatarRequest request) {
        accountService.updateAvatar(request);
        return ApiResponse.<AccountResponse>builder().build();
    }

    @PostMapping(value = "/edit-information")
    public ApiResponse<AccountResponse> editInfor(@RequestBody UpdateAccountInformationRequest request) {
        accountService.updateInformation(request);
        return ApiResponse.<AccountResponse>builder().build();
    }

    @PostMapping(value = "/set-password-prompt")
    public ApiResponse<?> setPasswordPrompt(@RequestBody SetPasswordPromptRequest request) {
        accountService.setPasswordPrompt(request);
        return ApiResponse.builder().build();
    }

    @GetMapping("/users")
    public ApiResponse<List<ListAccountResponse>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        List<ListAccountResponse> users = accountService.getAllUsers(page, size);
        return ApiResponse.<List<ListAccountResponse>>builder().result(users).build();
    }

    @GetMapping("/users-by-type/{memberType}")
    public ApiResponse<List<ListAccountResponse>> getUsersByType(@PathVariable MemberType memberType) {
        List<ListAccountResponse> users = accountService.getUserByType(memberType);
        return ApiResponse.<List<ListAccountResponse>>builder().result(users).build();
    }

    @PostMapping("/ban/{accountId}")
    public ApiResponse<Void> banAccount(@PathVariable int accountId) {
        return accountService.banAccount(accountId);
    }

    @PostMapping("/unlock/{accountId}")
    public ApiResponse<Void> unlockAccount(@PathVariable int accountId) {
        return accountService.unlockAccount(accountId);
    }

    @PostMapping("get-info-reset-password")
    public ApiResponse<ForgotPasswordResponse> getInfoResetPassword(@RequestBody ForgotPasswordRequest request) {
        return ApiResponse.<ForgotPasswordResponse>builder()
                .result(accountService.getEmailPhoneByUserName(request))
                .build();
    }
}
