package thebook.fshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AddVoucherRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.VoucherResponse;
import thebook.fshop.entity.Voucher;
import thebook.fshop.service.VoucherService;

@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoucherController {
    VoucherService voucherService;

    @GetMapping("/get-voucher-available")
    public ApiResponse<List<VoucherResponse>> getAllVouchers() {
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherService.getVoucherForCart())
                .build();
    }

    @PostMapping("/add-voucher")
    public  ApiResponse<?> addVoucher (@RequestBody AddVoucherRequest request) {
        voucherService.addVoucher(request);
        return ApiResponse.builder()
                .build();
    }

}
