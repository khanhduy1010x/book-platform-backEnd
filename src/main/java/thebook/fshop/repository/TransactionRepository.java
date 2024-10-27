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


    @Query(value = "SELECT a.accid, mp.package_name, " +
            "COALESCE(SUM(t.amount), 0) AS total_revenue " +
            "FROM public.membership_packages mp " +
            "LEFT JOIN public.user_member_ships ums ON mp.mp_id = ums.mp_id " +
            "LEFT JOIN public.accounts a ON ums.accid = a.accid " +
            "LEFT JOIN public.transactions t ON t.accid = a.accid AND t.transaction_type = 'PACKAGES' " +
            "WHERE mp.package_name IN (:packageNames) " +
            "GROUP BY a.accid, mp.package_name " +
            "ORDER BY total_revenue DESC", nativeQuery = true)
    List<Object[]> findTotalRevenueByPackageNames(@Param("packageNames") String packageNames);
}
