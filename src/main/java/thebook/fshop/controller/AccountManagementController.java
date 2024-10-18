package thebook.fshop.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.SetPasswordPromptRequest;
import thebook.fshop.DTO.Request.TypeRequest;
import thebook.fshop.DTO.Request.UpdateAccountInformationRequest;
import thebook.fshop.DTO.Request.UpdateAvatarRequest;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.DTO.Response.ApiResponse;
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

    // Existing endpoint to view the user's profile
    @GetMapping("/view-profile")
    ApiResponse<AccountResponse> viewProfile() {
        AccountResponse response = accountService.getMyInfo();
        return ApiResponse.<AccountResponse>builder().result(response).build();
    }

    // Existing endpoint to update avatar
    @PostMapping(
            value = "/edit-avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<AccountResponse> editAvatar(@ModelAttribute @Valid UpdateAvatarRequest request) {
        accountService.updateAvatar(request);
        return ApiResponse.<AccountResponse>builder().build();
    }

    // Existing endpoint to update account information
    @PostMapping(value = "/edit-information")
    public ApiResponse<AccountResponse> editInfor(@RequestBody UpdateAccountInformationRequest request) {
        accountService.updateInformation(request);
        return ApiResponse.<AccountResponse>builder().build();
    }

    // Existing endpoint to set password prompt
    @PostMapping(value = "/set-password-prompt")
    public ApiResponse<?> setPasswordPrompt(@RequestBody SetPasswordPromptRequest request) {
        accountService.setPasswordPrompt(request);
        return ApiResponse.builder().build();
    }

    // New endpoint to get all users
    @GetMapping("/users")
    public ApiResponse<List<Account>> getAllUsers() {
        List<Account> users = accountService.getAllUsers();
        return ApiResponse.<List<Account>>builder().result(users).build();
    }

    // New endpoint to find users by their type
    @GetMapping("/users/by-type")
    public ApiResponse<List<AccountResponse>> findUserByType(@RequestBody @Valid TypeRequest request) {
        log.info(request.toString());
        List<AccountResponse> users = accountService.findUserByType(request);
        return ApiResponse.<List<AccountResponse>>builder().result(users).build();
    }
}
