package thebook.fshop.repository;

import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {
    // Lấy danh sách giao dịch theo tài khoản
    List<Transaction> findByAccount_AccID(int accID);

    // Lấy lịch sử giao dịch theo tài khoản và số tiền
    List<Transaction> findByAccount_AccIDAndAmount(int accID, long amount);

    @Query("SELECT a " +
            "FROM Account a " +
            "JOIN Transaction t ON a.accID = t.account.accID " +
            "WHERE t.transactionType = 'INCREASE' " +
            "GROUP BY a.accID " +
            "ORDER BY SUM(t.amount) DESC")
    List<Account> findTop10User();

    @Query(value = """
        SELECT SUM(t.amount) 
        FROM public.transactions t 
        WHERE t.accid = :accountId AND t.transaction_type = 'INCREASE'
    """, nativeQuery = true)
    int findTotalPriceIncreaseByUser(@Param("accountId") int accountId);
}
