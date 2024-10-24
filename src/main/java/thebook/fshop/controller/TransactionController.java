package thebook.fshop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.TransactionResponse;
import thebook.fshop.service.TransactionService;

@RestController
@RequestMapping("/account/transaction")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TransactionController {
    TransactionService transactionService;

    /*@GetMapping("/history")
    public ApiResponse<List<TransactionResponse>> getTransactionHistory() {
        return ApiResponse.<List<TransactionResponse>>builder()
                .result(transactionService.getTransactionHistoryByAccount())
                .build();
    }*/
    @GetMapping("/history")
    public ApiResponse<List<TransactionResponse>> getTransactionHistory() {
        return ApiResponse.<List<TransactionResponse>>builder()
                .result(transactionService.getTransactionHistoryByAccount())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/history/detail")
    public ApiResponse<List<TransactionResponse>> getTransactionHistory(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String amount,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String time
    ) {
        List<TransactionResponse> response = transactionService.viewPaymentHistory(content, amount, transactionType, time);
        return ApiResponse.<List<TransactionResponse>>builder()
                .result(response)
                .build();
    }
}
