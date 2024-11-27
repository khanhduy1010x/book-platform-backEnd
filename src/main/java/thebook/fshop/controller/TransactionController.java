package thebook.fshop.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.*;
import thebook.fshop.entity.Transaction;
import thebook.fshop.service.TransactionService;

@RestController
@RequestMapping("/account/transaction")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TransactionController {
    TransactionService transactionService;

    @GetMapping("/history-member-package/{num}")
    public ApiResponse<ViewTransactionResponse> getTransactionHistoryMemberPackage
            ( @PathVariable int num,
                    @RequestParam(value = "page", defaultValue = "0") int page,
                    @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<ViewTransactionResponse>builder()
                .result(transactionService.getTransactionHistoryMemberPackageByAccount(num, page , size))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/history/detail")
    public ApiResponse<List<TransactionResponse>> getTransactionHistory(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String transactionType) {
        List<TransactionResponse> response = transactionService.viewPaymentHistory(search, filter, transactionType);
        return ApiResponse.<List<TransactionResponse>>builder().result(response).build();
    }

    @GetMapping("/admin/get-transaction/{num}")
    public ApiResponse<TransForAdminResponse> getTransaction(@PathVariable int num,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<TransForAdminResponse>builder()
                .result(transactionService.getTrans(num, page , size))
                .build();
    }
    @GetMapping("/search/{param}")
    public ApiResponse<ListTransForAdmin> searchTrans(@PathVariable String param) {
        return ApiResponse.<ListTransForAdmin>builder()
                .result(transactionService.search(param))
                .build();
    }
}
