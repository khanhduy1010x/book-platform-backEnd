package thebook.fshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.TransactionResponse;
import thebook.fshop.mapper.TransactionMapper;
import thebook.fshop.repository.TransactionRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TransactionService {

    TransactionMapper transactionMapper;
    TransactionRepository transactionRepository;
    SecurityService securityService;

    public List<TransactionResponse> getTransactionHistoryByAccount() {
        var account = securityService.getAccountByJWT();
        return transactionRepository.findByAccount_AccID(account.getAccID()).stream()
                .map(transactionMapper::toTransactionResponse)
                .toList();
    }
}
