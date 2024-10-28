package thebook.fshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thebook.fshop.entity.BookRate;

public interface BookRateRepository extends JpaRepository<BookRate, Integer> {
    // Tìm tất cả các đánh giá cho một cuốn sách cụ thể
    Page<BookRate> findByBook_ID(int bookID, Pageable pageable);
}
