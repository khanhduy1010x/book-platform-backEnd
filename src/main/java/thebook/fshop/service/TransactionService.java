package thebook.fshop.service;

import java.util.List;

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

    public List<TransactionResponse> viewPaymentHistory(String search, String filter, String transactionType) {
        // Tạo specification cho việc tìm kiếm và lọc
        Specification<Transaction> spec = Specification.where(null);

        // Tìm kiếm theo account ID hoặc content
        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("account").get("accID").as(String.class), "%" + search + "%"),
                            criteriaBuilder.like(root.get("content"), "%" + search + "%")
                    )
            );
        }

        // Lọc theo transactionType nếu có
        if (transactionType != null && !transactionType.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("transactionType"), transactionType)
            );
        }

        // Lọc theo filter: ví dụ lọc theo số tiền lớn hơn một mức nào đó
        if (filter != null && !filter.isEmpty()) {
            try {
                long amountFilter = Long.parseLong(filter);
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), amountFilter)
                );
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Filter must be a valid number");
            }
        }

        // Lấy danh sách transaction theo spec
        List<Transaction> transactions = transactionRepository.findAll(spec);

        // Chuyển từ entity Transaction sang DTO TransactionResponse
        return transactions.stream().map(transaction ->
                TransactionResponse.builder()
                        .ID(transaction.getID())
                        .accID(String.valueOf(transaction.getAccount().getAccID()))  // Lấy ID của account
                        .time(transaction.getTime())
                        .amount(transaction.getAmount())
                        .content(transaction.getContent())
                        .transactionType(transaction.getTransactionType())
                        .build()
        ).collect(Collectors.toList());
    }
}
