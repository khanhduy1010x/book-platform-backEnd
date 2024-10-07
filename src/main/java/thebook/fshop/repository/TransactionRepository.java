package thebook.fshop.repository;

import thebook.fshop.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    // Lấy danh sách giao dịch theo tài khoản
    List<Transaction> findByAccount_AccID(int accID);

    // Lấy lịch sử giao dịch theo tài khoản và số tiền
    List<Transaction> findByAccount_AccIDAndAmount(int accID, long amount);
}
