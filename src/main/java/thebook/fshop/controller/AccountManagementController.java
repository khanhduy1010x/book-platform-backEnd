package thebook.fshop.controller;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.UpdateAccountInformationRequest;
import thebook.fshop.DTO.Request.UpdateAvatarRequest;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.service.AccountService;

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
}
