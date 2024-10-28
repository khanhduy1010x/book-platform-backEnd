package thebook.fshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.TransactionResponse;
import thebook.fshop.mapper.TransactionMapper;
import thebook.fshop.repository.TransactionRepository;
import thebook.fshop.entity.Transaction;
import java.util.stream.Collectors;

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

    public Page<TransactionResponse> viewPaymentHistory(String content, String amountFilter, String transactionType, String time, Pageable pageable) {
        Specification<Transaction> spec = Specification.where(null);

        // Tìm kiếm theo content nếu có
        if (content != null && !content.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("content"), "%" + content + "%")
            );
        }

        // Lọc theo transactionType nếu có
        if (transactionType != null && !transactionType.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("transactionType"), transactionType)
            );
        }

        // Lọc theo amount nếu có
        if (amountFilter != null && !amountFilter.isEmpty()) {
            try {
                long amount = Long.parseLong(amountFilter);
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), amount)
                );
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Filter must be a valid number");
            }
        }

        // Lọc theo time nếu có
        if (time != null && !time.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("time"), time)
            );
        }

        // Lấy danh sách transaction theo spec
        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);

        // Chuyển từ entity Transaction sang DTO TransactionResponse
        return transactions.map(transaction ->
                TransactionResponse.builder()
                        .ID(transaction.getID())
                        .accID(String.valueOf(transaction.getAccount().getAccID()))
                        .time(transaction.getTime())
                        .amount(transaction.getAmount())
                        .content(transaction.getContent())
                        .transactionType(transaction.getTransactionType())
                        .build()
        );
    }

}
