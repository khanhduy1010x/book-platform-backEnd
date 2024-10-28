package thebook.fshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import thebook.fshop.entity.BookReadHistory;

public interface BooKReadHistoryRepository extends JpaRepository<BookReadHistory, Integer> {
    // Lấy danh sách lịch sử đọc sách theo tài khoản
    Page<BookReadHistory> findByAccount_AccID(int accID, Pageable pageable);

    // Lấy lịch sử đọc sách theo tài khoản và sách
    List<BookReadHistory> findByAccount_AccIDAndBook_ID(int accID, int ID);
}
