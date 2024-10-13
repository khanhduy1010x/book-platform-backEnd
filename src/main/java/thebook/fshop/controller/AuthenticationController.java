package thebook.fshop.controller;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import thebook.fshop.DTO.Request.*;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.AuthenticationResponse;
import thebook.fshop.DTO.Response.IntrorespectResponse;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.service.AccountService;
import thebook.fshop.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {

    AuthenticationService authenticationService;
    AccountService accountService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authentication(@RequestBody @Valid AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/register")
    ApiResponse<AccountResponse> createAccount(@RequestBody @Valid AccountCreationRequest request) {
        ApiResponse<AccountResponse> response = new ApiResponse<>();
        response.setResult(accountService.createAccount(request));
        return response;
    }

    @PostMapping("/sendSMS")
    ApiResponse sendOTP(@RequestBody @Valid SendOTPRequest request) {
        authenticationService.sendOTPSMS(request);
        return ApiResponse.builder().build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrorespectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrorespectResponse>builder().result(result).build();
    }

    @PostMapping("logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/my-info")
    ApiResponse<AccountResponse> myInfo() {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.getMyInfo())
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var respone = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(respone).build();
    }

    @PostMapping("/changePassword")
    ApiResponse<AuthenticationResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request)
            throws ParseException, JOSEException {
        authenticationService.changePassword(request);
        return ApiResponse.<AuthenticationResponse>builder().build();
    }

    @PostMapping("/verify-google")
    public Mono<ApiResponse<AuthenticationResponse>> verifyGoogleToken(@RequestBody GoogleLoginRequest request) {
        log.info("here");
        return authenticationService
                .getUserByGoogleToken(request)
                .map(authResponse -> ApiResponse.<AuthenticationResponse>builder()
                        .result(authResponse)
                        .build())
                .onErrorResume(error -> {
                    throw new AppException(ErrorCode.SERVER_ERROR);
                });
    }

    @PostMapping("/create-password")
    public ApiResponse<?> createPassword (@RequestBody CreatePasswordRequest request) {
        authenticationService.createPassword(request);
        return ApiResponse.builder().build();
    }
}
