package thebook.fshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Transaction;
import thebook.fshop.helper.MethodType;
import thebook.fshop.helper.TransactionType;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {
    // Lấy danh sách giao dịch theo tài khoản
    Page<Transaction> findByAccount_AccIDAndTransactionTypeOrderByIDDesc(int accID, TransactionType type, Pageable pageable);
    Page<Transaction>  findByAccount_AccID(int accID,Pageable pageable);
    List<Transaction> findByAccount_AccIDAndTransactionType(int accID, TransactionType type);
    List<Transaction>  findByAccount_AccIDOrderByIDDesc(int accID);
    @Query("SELECT a " +
            "FROM Account a " +
            "JOIN Transaction t ON a.accID = t.account.accID " +
            "GROUP BY a.accID " +
            "ORDER BY SUM(ABS(t.afterAmount - t.beforeAmount) + COALESCE(t.priceQR, 0)) DESC")
    List<Account> findTop10UsersWithHighestTransactions();

    @Query(value = """
    SELECT SUM(ABS(t.after_amount - t.before_amount) + COALESCE(t.priceqr, 0)) 
    FROM public.transactions t 
    WHERE t.accid = :accountId
""", nativeQuery = true)
    int findTotalTransactionValueByUser(@Param("accountId") int accountId);


    @Query(value = """
    SELECT a.accid, 
           mp.package_name, 
           COALESCE(SUM(ABS(t.after_amount - t.before_amount) + t.priceqr), 0) AS total_revenue 
    FROM public.membership_packages mp 
    LEFT JOIN public.user_member_ships ums ON mp.mp_id = ums.mp_id 
    LEFT JOIN public.accounts a ON ums.accid = a.accid 
    LEFT JOIN public.transactions t ON t.accid = a.accid AND t.transaction_type = 'MEMBER_PACKAGE' 
    WHERE mp.package_name IN (:packageNames) 
    GROUP BY a.accid, mp.package_name 
    ORDER BY total_revenue DESC
""", nativeQuery = true)
    List<Object[]> findTotalRevenueByPackageNames(@Param("packageNames") String packageNames);
    @Query("SELECT t.methodType, COUNT(t) " +
            "FROM Transaction t " +
            "GROUP BY t.methodType")
    List<Object[]> countMethodType();
    @Query("SELECT t.transactionType, COUNT(t) " +
            "FROM Transaction t " +
            "GROUP BY t.transactionType")
    List<Object[]> countTransactionType();

    Page<Transaction> findAllByTransactionType(TransactionType transactionType, Pageable pageable);
    Page<Transaction> findAllByMethodType(MethodType methodType, Pageable pageable);
    Page<Transaction> findAll(Pageable pageable);
    @Query(value = """
    SELECT t.* 
    FROM transactions t
    JOIN accounts a ON a.accid = t.accid
    WHERE (unaccent(a.username) LIKE unaccent(concat('%', :searchTerm, '%')) OR a.username LIKE concat('%', :searchTerm, '%'))
    OR (unaccent(a.phone) LIKE unaccent(concat('%', :searchTerm, '%')) OR a.phone LIKE concat('%', :searchTerm, '%'))
    OR (unaccent(a.email) LIKE unaccent(concat('%', :searchTerm, '%')) OR a.email LIKE concat('%', :searchTerm, '%'))
    OR (unaccent(a.full_name) LIKE unaccent(concat('%', :searchTerm, '%')) OR a.full_name LIKE concat('%', :searchTerm, '%'))
    """, nativeQuery = true)
    List<Transaction> findByUsernamePhoneEmailFullName(@Param("searchTerm") String searchTerm);

}
