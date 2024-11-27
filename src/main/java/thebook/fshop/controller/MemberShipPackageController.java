package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.BuyPackageRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.MemberPackageForAdminResponse;
import thebook.fshop.DTO.Response.OrderForAdminResponse;
import thebook.fshop.DTO.Response.SaleForPackageResponse;
import thebook.fshop.entity.MembershipPackage;
import thebook.fshop.service.MembershipPackageService;

import java.util.List;

@RestController
@RequestMapping("/member-ship")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MemberShipPackageController {

    MembershipPackageService membershipPackageService;

    @GetMapping("/get-all")
    public ApiResponse<List<MembershipPackage>> getAll() {
        return ApiResponse.<List<MembershipPackage>>builder()
                .result(membershipPackageService.getAllPackage())
                .build();
    }

    @PostMapping("/buy-package-by-amount")
    public ApiResponse<?> buyPackageByAmount(@RequestBody BuyPackageRequest request) {
        membershipPackageService.buyMemberShipPackage(request);
        return ApiResponse.builder()
                .build();
    }

    @GetMapping("check-ownership-package/{id}")
    public ApiResponse<SaleForPackageResponse> checkPackaged(@PathVariable int id) {
        return ApiResponse.<SaleForPackageResponse>builder()
                .result( membershipPackageService.getSaleForPackage(id)
)
                .build();

    }
    @GetMapping("/admin/get-all-mp/{num}")
    public ApiResponse<MemberPackageForAdminResponse> getAllOrderAdmin(
            @PathVariable int num,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        var result = membershipPackageService.getAllMPForAdmin(num, page, size);
        return ApiResponse.<MemberPackageForAdminResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/add-new")
    public ApiResponse<?> addNewPkg(@RequestBody MembershipPackage request){
      membershipPackageService.addNewMemberPackage(request);
        return ApiResponse.builder()
                .build();
    }
    @PostMapping("/edit-pkg")
    public ApiResponse<?> editPkg(@RequestBody MembershipPackage request){
        membershipPackageService.editPkg(request);
        return ApiResponse.builder()
                .build();
    }



}
