package thebook.fshop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.MemberTypeRequest;
import thebook.fshop.DTO.Response.*;
import thebook.fshop.entity.Transaction;
import thebook.fshop.helper.MethodType;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.TransactionType;
import thebook.fshop.mapper.AccountMapper;
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
    AccountMapper accountMapper;
    public ViewTransactionResponse getTransactionHistoryMemberPackageByAccount(int num, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        var account = securityService.getAccountByJWT();
        if(num == 4) {
          var result =  transactionRepository.findByAccount_AccID(account.getAccID(),pageable);
        var totalResult =  transactionRepository.findByAccount_AccIDOrderByIDDesc(account.getAccID());
            return ViewTransactionResponse.builder()
                    .listTrans(result.getContent())
                    .totalRecords(totalResult.size())
                    .totalPages(result.getTotalPages())
                    .currentPage(result.getNumber())
                    .build();
        }
        var trans = TransactionType.values()[num];
               var result = transactionRepository.findByAccount_AccIDAndTransactionTypeOrderByIDDesc(account.getAccID(), trans, pageable);
        var totalResult =transactionRepository.findByAccount_AccIDAndTransactionType(account.getAccID(), trans);

        return ViewTransactionResponse.builder()
                .listTrans(result.getContent())
                .totalPages(result.getTotalPages())
                .totalRecords(totalResult.size())
                .currentPage(result.getNumber())
                .build();
    }

    public TransForAdminResponse getTrans(int num, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if(num == -1) {
            var list = transactionRepository.findAll(pageable);
            return TransForAdminResponse.builder()
                    .countTransByMethodTypes(getMethodTypeCounts())
                    .countTransByTransType(getTransTypeCounts())
                    .currentPage(list.getNumber())
                    .totalPages(list.getTotalPages())
                    .listTrans(list.getContent())
                    .build();
        }
        if(num >= 0 && num <= 2) {
            var list = transactionRepository.findAllByTransactionType(TransactionType.values()[num],pageable);
            return TransForAdminResponse.builder()
                    .countTransByMethodTypes(getMethodTypeCounts())
                    .countTransByTransType(getTransTypeCounts())
                    .currentPage(list.getNumber())
                    .totalPages(list.getTotalPages())
                    .listTrans(list.getContent())
                    .build();
        }
        var list = transactionRepository.findAllByMethodType(num == 3 ? MethodType.QR_CODE : MethodType.WEBSITE,pageable);
        return TransForAdminResponse.builder()
                .countTransByMethodTypes(getMethodTypeCounts())
                .countTransByTransType(getTransTypeCounts())
                .currentPage(list.getNumber())
                .totalPages(list.getTotalPages())
                .listTrans(list.getContent())
                .build();
    }



    public List<TransactionResponse> viewPaymentHistory(String search, String filter, String transactionType) {
        // Tạo specification cho việc tìm kiếm và lọc
        Specification<Transaction> spec = Specification.where(null);

        // Tìm kiếm theo account ID hoặc content
        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(root.get("account").get("accID").as(String.class), "%" + search + "%"),
                    criteriaBuilder.like(root.get("content"), "%" + search + "%")));
        }

        // Lọc theo transactionType nếu có
        if (transactionType != null && !transactionType.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("transactionType"), transactionType));
        }

        // Lọc theo filter: ví dụ lọc theo số tiền lớn hơn một mức nào đó
        if (filter != null && !filter.isEmpty()) {
            try {
                long amountFilter = Long.parseLong(filter);
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), amountFilter));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Filter must be a valid number");
            }
        }

        // Lấy danh sách transaction theo spec
        List<Transaction> transactions = transactionRepository.findAll(spec);

        // Chuyển từ entity Transaction sang DTO TransactionResponse
        return transactions.stream()
                .map(transaction -> TransactionResponse.builder()
                        .ID(transaction.getID())
                        .accID(String.valueOf(transaction.getAccount().getAccID())) // Lấy ID của account
                        .time(transaction.getTime())
                        .amount(transaction.getAfterAmount())
                        .content(transaction.getContent())
                        .transactionType(transaction.getTransactionType())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ListStatisticPayMostResponse> getStatisticsPayMostReader() {
        List<AccountResponse> topUsers = transactionRepository.findTop10UsersWithHighestTransactions().stream().map(accountMapper::toAccountResponse).toList();
        return topUsers.stream()
                .map(user -> {
                    int total_price = transactionRepository.findTotalTransactionValueByUser(user.getAccID());
                    return ListStatisticPayMostResponse.builder()
                            .account(user)
                            .total_price(total_price)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<ListStatisticsByMembershipPackageResponse> getStatisticsMembershipPackage(MemberTypeRequest request) {
        // Fetch the result from the repository
        List<Object[]> results = transactionRepository.findTotalRevenueByPackageNames(String.valueOf(request.getMemberType()));
        // Map the results to ListStatisticsByMembershipPackageResponse objects
        return results.stream()
                .map(result -> {
                    int accId = (int) result[0];                 // Assuming accId is of type Long
                    String packageName = (String) result[1];        // Package name from the result
                    BigDecimal totalRevenue = (BigDecimal) result[2]; // Total revenue from the result

                    // Create a new response object and populate it with the result
                    return new ListStatisticsByMembershipPackageResponse(accId, packageName, totalRevenue);
                })
                .collect(Collectors.toList());
    }
    private List<CountTransByMethodType> getMethodTypeCounts() {
        List<Object[]> rawCounts = transactionRepository.countMethodType();

        Map<MethodType, Long> countsMap = rawCounts.stream()
                .collect(Collectors.toMap(
                        row -> (MethodType) row[0],
                        row -> (Long) row[1]
                ));
        for (MethodType type : MethodType.values()) {
            countsMap.putIfAbsent(type, 0L);
        }

        return countsMap.entrySet().stream()
                .map(entry -> CountTransByMethodType.builder()
                        .methodType(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
    private List<CountTransByTransType> getTransTypeCounts() {
        List<Object[]> rawCounts = transactionRepository.countTransactionType();

        Map<TransactionType, Long> countsMap = rawCounts.stream()
                .collect(Collectors.toMap(
                        row -> (TransactionType) row[0],
                        row -> (Long) row[1]
                ));
        for (TransactionType type : TransactionType.values()) {
            countsMap.putIfAbsent(type, 0L);
        }

        return countsMap.entrySet().stream()
                .map(entry -> CountTransByTransType.builder()
                        .transactionType(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public ListTransForAdmin search (String param) {
        return ListTransForAdmin.builder()
                .listTrans(transactionRepository.findByUsernamePhoneEmailFullName(param))
                .build();

    }
}
