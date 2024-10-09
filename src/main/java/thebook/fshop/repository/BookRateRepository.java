package thebook.fshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import thebook.fshop.entity.BookRate;

public interface BookRateRepository extends JpaRepository<BookRate, Integer> {
    // Tìm tất cả các đánh giá cho một cuốn sách cụ thể
    List<BookRate> findByBook_ID(int bookID);
}
