package thebook.fshop.repository;

import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.BookReadHistory;

public interface BooKReadHistoryRepository extends JpaRepository<BookReadHistory, Integer> {
    // Lấy danh sách lịch sử đọc sách theo tài khoản
    List<BookReadHistory> findByAccount_AccID(int accID);

    // Lấy lịch sử đọc sách theo tài khoản và sách
    List<BookReadHistory> findByAccount_AccIDAndBook_ID(int accID, int ID);

    @Query("SELECT b.author, COUNT(brh.id) AS read_count " +
            "FROM Book AS b " +
            "JOIN BookReadHistory AS brh ON b.ID = brh.id " +
            "WHERE brh.account.accID = :accountId " +  // Sử dụng ':accountId' để đặt tham số
            "GROUP BY b.author " +
            "ORDER BY read_count DESC " +
            "LIMIT 1")
    List<Object[]> findMostReadAuthorByAccount(@Param("accountId") int accountId);

    @Query("SELECT c.cateName, COUNT(brh.id) AS read_count " +
            "FROM Category c " +
            "JOIN Book b ON c.ID = b.category.ID " +
            "JOIN BookReadHistory brh ON b.ID = brh.id " +
            "WHERE brh.account.accID = :accountId " +
            "GROUP BY c.ID, c.cateName " +
            "ORDER BY read_count DESC")
    List<Object[]> findMostReadCategoryByAccount(@Param("accountId") int accountId);

    @Query("SELECT b FROM Book b WHERE b.author = :author")
    List<Book> findBooksByAuthor(@Param("author") String author);

    @Query("SELECT b FROM Book b JOIN Category c ON b.category.ID = c.ID WHERE c.cateName = :category")
    List<Book> findBooksByCategory(@Param("category") String category);

}
