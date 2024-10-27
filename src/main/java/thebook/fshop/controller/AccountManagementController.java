package thebook.fshop.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.*;
import thebook.fshop.DTO.Response.*;
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
    public ApiResponse<CustomPageResponse<ListAccountResponse>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<ListAccountResponse> usersPage = (Page<ListAccountResponse>) accountService.getAllUsers(page, size);

        CustomPageResponse<ListAccountResponse> customPageResponse = CustomPageResponse.<ListAccountResponse>builder()
                .pageNumber(usersPage.getNumber())
                .totalPages(usersPage.getTotalPages())
                .totalElements(usersPage.getTotalElements())
                .content(usersPage.getContent())
                .build();

        return ApiResponse.<CustomPageResponse<ListAccountResponse>>builder()
                .result(customPageResponse)
                .build();
    }

    @GetMapping("/users-by-type/{memberType}")
    public ApiResponse<CustomPageResponse<ListAccountResponse>> getUsersByType(
            @PathVariable MemberType memberType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<ListAccountResponse> usersPage = (Page<ListAccountResponse>) accountService.getUserByType(memberType, page, size);

        CustomPageResponse<ListAccountResponse> customPageResponse = CustomPageResponse.<ListAccountResponse>builder()
                .pageNumber(usersPage.getNumber())
                .totalPages(usersPage.getTotalPages())
                .totalElements(usersPage.getTotalElements())
                .content(usersPage.getContent())
                .build();

        return ApiResponse.<CustomPageResponse<ListAccountResponse>>builder()
                .result(customPageResponse)
                .build();
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
