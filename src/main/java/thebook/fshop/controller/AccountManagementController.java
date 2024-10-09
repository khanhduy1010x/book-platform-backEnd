package thebook.fshop.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import thebook.fshop.DTO.Request.AvatarRequest;
import thebook.fshop.DTO.Request.UpdateAccountInformationRequest;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.AuthenticationResponse;
import thebook.fshop.service.AccountService;
import thebook.fshop.validation.FileValidation;

import java.util.Date;

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
    @PostMapping(value = "/edit-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<AccountResponse> editAvatar(@RequestParam(value = "avatar", required = false) @FileValidation MultipartFile avatar) {
        var request = new AvatarRequest(avatar);
        accountService.updateAvatar(request);
        return ApiResponse.<AccountResponse>builder().build();
    }
    @PostMapping(value = "/edit-information")
    public ApiResponse<AccountResponse> editInfor(@RequestBody UpdateAccountInformationRequest request) {
        accountService.updateInformation(request);
        return ApiResponse.<AccountResponse>builder().build();
    }
}
