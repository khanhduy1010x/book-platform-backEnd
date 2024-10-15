package thebook.fshop.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import thebook.fshop.DTO.Request.MemberRoleUpRequest;

import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.DTO.Response.ApiResponse;

import thebook.fshop.service.AccountService;

@RestController
@RequestMapping("/memberrole")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MemberRoleController {
    AccountService accountService;

    @PostMapping("/up")
    public ApiResponse<AccountResponse> upRole(@RequestBody @Valid MemberRoleUpRequest request) {
        accountService.upRole(request);
        return ApiResponse.<AccountResponse>builder().build();
    }

}
