package thebook.fshop.repository;

import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import thebook.fshop.DTO.Response.ListReadBookStatisticResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.BookReadHistory;

public interface BooKReadHistoryRepository extends JpaRepository<BookReadHistory, Integer> {
    // Lấy danh sách lịch sử đọc sách theo tài khoản
    List<BookReadHistory> findByAccount_AccID(int accID);

    // Lấy lịch sử đọc sách theo tài khoản và sách
    List<BookReadHistory> findByAccount_AccIDAndBook_ID(int accID, int ID);
    // Custom query to fetch top 10 most read books
    @Query("SELECT b FROM Book b JOIN BookReadHistory brh ON b.ID = brh.book.ID GROUP BY b.ID ORDER BY COUNT(brh.id) DESC")
    List<Book> findMostReadBooks();

    // Method to find the total quantity of users who read a specific book by its ID
    @Query("SELECT COUNT(DISTINCT brh.account.accID) FROM BookReadHistory brh WHERE brh.book.ID = :bookId")
    int findTotalQuantityByBookId(@Param("bookId") Integer bookId);

}
