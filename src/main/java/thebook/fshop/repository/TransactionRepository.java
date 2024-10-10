package thebook.fshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import thebook.fshop.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    // Lấy danh sách giao dịch theo tài khoản
    List<Transaction> findByAccount_AccID(int accID);

    // Lấy lịch sử giao dịch theo tài khoản và số tiền
    List<Transaction> findByAccount_AccIDAndAmount(int accID, long amount);
}
