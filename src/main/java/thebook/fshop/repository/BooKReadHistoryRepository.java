package thebook.fshop.repository;

import thebook.fshop.entity.BookReadHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BooKReadHistoryRepository extends JpaRepository<BookReadHistory, Integer> {
    // Lấy danh sách lịch sử đọc sách theo tài khoản
    List<BookReadHistory> findByAccount_AccID(int accID);

    // Lấy lịch sử đọc sách theo tài khoản và sách
    List<BookReadHistory> findByAccount_AccIDAndBook_ID(int accID, int ID);
}
