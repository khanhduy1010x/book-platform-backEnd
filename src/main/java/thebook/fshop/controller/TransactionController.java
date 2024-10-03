package thebook.fshop.controller;

import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.TransactionResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Transaction;
import thebook.fshop.helper.TransactionType;  // Bổ sung import này
import thebook.fshop.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction-history")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // API để thêm giao dịch vào lịch sử giao dịch
    @PostMapping("/add")
    public void addTransactionHistory(@RequestParam int accID, @RequestParam long amount,
                                      @RequestParam String content, @RequestParam TransactionType type) {
        Account account = new Account();
        account.setAccID(accID); // Sử dụng đối tượng Account

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setContent(content);
        transaction.setTransactionType(type);

        transactionService.addTransactionHistory(account, transaction);
    }

    // API để lấy lịch sử giao dịch của tài khoản
    @GetMapping("/account/history")
    public ApiResponse<List<TransactionResponse>> getTransactionHistory() {
        return ApiResponse.<List<TransactionResponse>>builder()
                .result(transactionService.getTransactionHistoryByAccount())
                .build();
    }

    // API để lấy lịch sử giao dịch theo tài khoản và số tiền
    @GetMapping("/account/{accID}/amount/{amount}")
    public List<TransactionResponse> getTransactionHistoryByAccountAndAmount(
            @PathVariable int accID, @PathVariable long amount) {
        return transactionService.getTransactionHistoryByAccountAndAmount(accID, amount);
    }
}
