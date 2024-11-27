package thebook.fshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AddVoucherRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.ListVoucherResponse;
import thebook.fshop.DTO.Response.VoucherForAdminResponse;
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
    @GetMapping("/get-all-admin")
    public  ApiResponse<VoucherForAdminResponse> getALlVoucher(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<VoucherForAdminResponse>builder()
                .result(voucherService.getAllVouchers(page,size))
                .build();
    }
    @GetMapping("/search/{param}")
    public ApiResponse<ListVoucherResponse> searchVoucher(@PathVariable String param) {
        return ApiResponse.<ListVoucherResponse>builder()
                .result(voucherService.searchVoucher(param))
                .build();
    }
    @PostMapping("/add-new-voucher")
    public ApiResponse<?> addNewVoucher(@RequestBody Voucher voucher) {
        voucherService.addNewVoucher(voucher);
        return ApiResponse.builder()
                .build();

    }

}
