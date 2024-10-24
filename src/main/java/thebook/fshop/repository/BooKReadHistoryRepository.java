package thebook.fshop.repository;

import java.awt.print.Pageable;
import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import thebook.fshop.DTO.Response.ListReadBookStatisticResponse;
import thebook.fshop.DTO.Response.ListReaderStatisticResponse;
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

    @Query(value = "SELECT brh.* " +
            "FROM public.book_read_histories brh " +
            "JOIN (SELECT accid, COUNT(bookid) AS total_books_read " +
            "FROM public.book_read_histories " +
            "GROUP BY accid " +
            "ORDER BY total_books_read DESC " +
            "LIMIT 10) AS top_readers " +
            "ON brh.accid = top_readers.accid", nativeQuery = true)
    List<BookReadHistory> findTop10Readers();

    // Get the total number of distinct books read by a specific reader
    @Query("SELECT COUNT(b.book.ID) FROM BookReadHistory b WHERE b.account.accID = :readerId")
    int findTotalBookByReader(@Param("readerId") Integer readerId);

}
