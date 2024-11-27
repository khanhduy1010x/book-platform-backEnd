package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.CheckQROrderRequest;
import thebook.fshop.DTO.Request.GetQrMemberShipRequest;
import thebook.fshop.DTO.Request.OrderCreationRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.QRCodeResponse;
import thebook.fshop.DTO.Response.StatusQROrderResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.helper.TempAddress;
import thebook.fshop.service.AccountService;
import thebook.fshop.service.PaymentQRService;

import java.util.Map;

@RestController
@RequestMapping("/paymentQR")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentQRController {

    PaymentQRService paymentQRService;
    AccountService accountService;

    @PostMapping("/get-qr-order")
    public ApiResponse<QRCodeResponse> getQrOrder(@RequestBody OrderCreationRequest request) {
        log.info("Request: {}",request);
        return ApiResponse.<QRCodeResponse>builder()
                .result(paymentQRService.createQrURLAndSave(request))
                .build();
    }

    @PostMapping("/process-sms-bank")
    public ApiResponse<?> handleTelegramUpdate(@RequestBody Map<String, String> payload) {
        paymentQRService.processSMS(payload.get("text"));
        return ApiResponse.builder().build();
    }

    @PostMapping("/check-payment-qr-status")
    public ApiResponse<StatusQROrderResponse> checkPaymentQrStatus(@RequestBody CheckQROrderRequest request) {
        return ApiResponse.<StatusQROrderResponse>builder()
                .result(paymentQRService.checkStatusQRBank(request))
                .build();
    }

    @PostMapping("/get-qr-member-package")
    public ApiResponse<QRCodeResponse> getQrOrder(@RequestBody GetQrMemberShipRequest request) {
        return ApiResponse.<QRCodeResponse>builder()
                .result(paymentQRService.getQrURL(request))
                .build();
    }

    @PostMapping("/check-payment-package-qr-status")
    public ApiResponse<StatusQROrderResponse> checkPaymentPackageQrStatus(@RequestBody CheckQROrderRequest request) {
        return ApiResponse.<StatusQROrderResponse>builder()
                .result(paymentQRService.checkPaymentStatusQRBank(request))
                .build();
    }
}
