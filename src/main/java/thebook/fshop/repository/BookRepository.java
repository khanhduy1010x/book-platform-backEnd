package thebook.fshop.repository;

import java.util.List;
import java.util.Optional;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBookByCategory_ID(int ID);

    @Query(
            "SELECT b FROM Book b WHERE  LOWER(b.bookName) LIKE %:query% OR  LOWER(b.author) LIKE %:query% OR  LOWER(b.category.cateName) LIKE %:query%")
    List<Book> findByBookNameAndAuthorAndMemberType(String query);
    Optional<Book> findByID(int bookID);
    @Query(value = "SELECT b.bookid, b.book_name, b.author, b.cover_image, b.description, b.price, b.book_type, " +
            "b.ebook_type, b.member_type, b.cateid, b.url, SUM(od.quantity) AS total_quantity " +
            "FROM books b " +
            "JOIN order_details od ON b.bookid = od.bookid " +
            "JOIN orders o ON od.orderid = o.orderid " +
            "GROUP BY b.bookid, b.book_name, b.author, b.cover_image, b.description, " +
            "b.price, b.book_type, b.ebook_type, b.member_type, b.cateid, b.url " +
            "ORDER BY total_quantity DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Book> findTop10MostPurchasedBooks();
    @Query("SELECT a FROM Account a JOIN BookRate br ON a.accID = br.account.accID GROUP BY a.accID ORDER BY COUNT(br.id) DESC")
    List<Account> findTop10Content();

    // Repository method to find total contributions by account
    @Query("SELECT COUNT(br.id) FROM BookRate br WHERE br.account.accID = :accountId GROUP BY br.account.accID")
    int findTotalContentByAccount(@Param("accountId") int accountId);
}
