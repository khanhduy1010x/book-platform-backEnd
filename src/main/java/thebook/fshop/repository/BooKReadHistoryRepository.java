package thebook.fshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.BookReadHistory;

public interface BooKReadHistoryRepository extends JpaRepository<BookReadHistory, Integer> {
    // Lấy danh sách lịch sử đọc sách theo tài khoản
    @Query("SELECT new thebook.fshop.DTO.Response.BookReadHistoryResponse( " +
            "brh.id, b, a.accID, brh.readPercent) " +
            "FROM BookReadHistory brh " +
            "JOIN brh.book b " +
            "JOIN brh.account a " +
            "WHERE a.accID = :accId")
    List<BookReadHistoryResponse> findByAccountId(@Param("accId") int accId);
    // Lấy lịch sử đọc sách theo tài khoản và sách
    BookReadHistory findByAccount_AccIDAndBook_ID(int accID, int ID);
    @Query("SELECT b.author.name  , COUNT(brh.id) AS read_count " +
            "FROM Book AS b " +
            "JOIN BookReadHistory AS brh ON b.ID = brh.id " +
            "WHERE brh.account.accID = :accountId " +  // Sử dụng ':accountId' để đặt tham số
            "GROUP BY b.author.name " +
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

    @Query("SELECT b FROM Book b JOIN Author a ON a.id = b.author.id WHERE b.author.name = :author")
    List<Book> findBooksByAuthor(@Param("author") String author);

    @Query("SELECT b FROM Book b JOIN Category c ON b.category.ID = c.ID WHERE c.cateName = :category")
    List<Book> findBooksByCategory(@Param("category") String category);
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
