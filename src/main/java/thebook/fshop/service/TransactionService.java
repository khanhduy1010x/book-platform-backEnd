package thebook.fshop.service;

import java.util.Date;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.TransactionResponse;
import thebook.fshop.entity.Transaction;
import thebook.fshop.entity.Account;
import thebook.fshop.mapper.TransactionMapper;
import thebook.fshop.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TransactionService {

    TransactionMapper transactionMapper;
    TransactionRepository transactionRepository;
    SecurityService securityService;

    // Thêm lịch sử giao dịch
    public void addTransactionHistory(Account account, Transaction transaction) {
        transaction.setAccount(account);
        transaction.setTime(new Date()); // Set thời gian hiện tại cho giao dịch
        transactionRepository.save(transaction);
    }

    // Lấy danh sách lịch sử giao dịch theo tài khoản
    public List<TransactionResponse> getTransactionHistoryByAccount() {
        var account = securityService.getAccountByJWT();
        return transactionRepository.findByAccount_AccID(account.getAccID()).stream()
                .map(transactionMapper::toTransactionResponse)
                .toList();
    }

    // Lấy lịch sử giao dịch theo tài khoản và số tiền
    public List<TransactionResponse> getTransactionHistoryByAccountAndAmount(int accID, long amount) {
        return transactionRepository.findByAccount_AccIDAndAmount(accID, amount).stream()
                .map(transactionMapper::toTransactionResponse)
                .toList();
    }
}
