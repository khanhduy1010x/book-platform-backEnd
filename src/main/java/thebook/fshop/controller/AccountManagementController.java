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
import thebook.fshop.entity.Account;
import thebook.fshop.helper.Role;
import thebook.fshop.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountManagementController {
    AccountService accountService;

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
    public ApiResponse<List<Account>> getAllUsers() {
        List<Account> users = accountService.getAllUsers();
        return ApiResponse.<List<Account>>builder().result(users).build();
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
