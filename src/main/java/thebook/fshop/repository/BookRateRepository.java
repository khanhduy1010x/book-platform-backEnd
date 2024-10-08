package thebook.fshop.repository;

import thebook.fshop.entity.BookRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRateRepository extends JpaRepository<BookRate, Integer> {
    // Tìm tất cả các đánh giá cho một cuốn sách cụ thể
    List<BookRate> findByBook_ID(int bookID);
}
