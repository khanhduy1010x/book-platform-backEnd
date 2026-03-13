package thebook.fshop.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import thebook.fshop.DTO.Response.BookDetailResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Category;
import thebook.fshop.helper.BookType;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBookByCategory_IDAndIsVisible(int ID,boolean isVisible);
    List<Book> findBookByBookTypeAndIsVisible(BookType type,boolean isVisible);
    Page<Book> findBookByBookTypeOrderByIDAsc(BookType type, Pageable pageable);

    @Query(
            "SELECT b FROM Book b WHERE " +
                    "LOWER(b.bookName) ILIKE %:query% " +
                    "OR LOWER(b.author.name) ILIKE %:query% " +
                    "OR LOWER(b.category.cateName) ILIKE %:query%"
    )
    List<Book> findByBookNameAndAuthorAndCategory(String query);
    @Query(
            "SELECT b FROM Book b WHERE " +
                    "(LOWER(b.bookName) ILIKE %:query% " +
                    "OR LOWER(b.author.name) ILIKE %:query% " +
                    "OR LOWER(b.category.cateName) ILIKE %:query%) " +
                    "AND b.isVisible = true"
    )
    List<Book> findByBookNameAndAuthorAndCategoryUser(String query);

    List<Book> findBookByCategory_IDAndBookTypeAndIsVisibleOrderByMemberTypeDesc(int cateID, BookType bookType,boolean isVisible);

    Optional<Book> findByID(int bookID);
    @Query("SELECT DISTINCT b.category FROM Book b WHERE b.bookType = :bookType")
    List<Category> findCateIdsByBookType(BookType bookType);



    @Query(value = "SELECT b.bookid, b.book_name, b.authorid, b.cover_image, b.is_visible, b.description, b.price, b.book_type, " +
            "b.ebook_type, b.member_type, b.cateid, b.url, SUM(od.quantity) AS total_quantity " +
            "FROM books b " +
            "JOIN order_details od ON b.bookid = od.bookid " +
            "JOIN orders o ON od.orderid = o.orderid " +
            "GROUP BY b.bookid, b.book_name, b.authorid,b.is_visible ,b.cover_image, b.description, " +
            "b.price, b.book_type, b.ebook_type, b.member_type, b.cateid, b.url " +
            "ORDER BY total_quantity DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Book> findTop10MostPurchasedBooks();







    @Query("SELECT a FROM Account a JOIN BookRate br ON a.accID = br.account.accID GROUP BY a.accID ORDER BY COUNT(br.id) DESC")
    List<Account> findTop10Content();

    // Repository method to find total contributions by account
    @Query("SELECT COUNT(br.id) FROM BookRate br WHERE br.account.accID = :accountId GROUP BY br.account.accID")
    int findTotalContentByAccount(@Param("accountId") int accountId);

    @Query(value = "SELECT DATE(o.date) AS order_date, " +
            "b.bookid AS id, " + // Ensure this returns an integer
            "b.book_name AS book_name, " +
            "b.authorid AS author, " +
            "b.url AS url, " +
            "b.cover_image AS cover_image, " +
            "SUM(od.quantity * b.price) AS total_revenue " +
            "FROM public.order_details od " +
            "JOIN public.books b ON od.bookid = b.bookid " +
            "JOIN public.orders o ON od.orderid = o.orderid " +
            "WHERE o.payment_status = 'COMPLETED' " +
            "AND DATE(o.date) BETWEEN :stDate AND :edDate " +
            "GROUP BY DATE(o.date), b.bookid, b.book_name, b.authorid, b.url, b.cover_image " +
            "ORDER BY order_date ASC, total_revenue DESC", nativeQuery = true)
    List<Object[]> findRevenueByDateRange(@Param("stDate") Date stDate, @Param("edDate") Date edDate);
    @Query(value = "SELECT DATE(o.date) AS order_date, " +
            "b.bookid AS id, " +
            "b.book_name AS book_name, " +
            "b.authorid AS author, " +
            "b.url AS url, " +
            "b.cover_image AS cover_image, " +
            "SUM(od.quantity * b.price) AS total_revenue " +
            "FROM public.order_details od " +
            "JOIN public.books b ON od.bookid = b.bookid " +
            "JOIN public.orders o ON od.orderid = o.orderid " +
            "WHERE o.payment_status = 'COMPLETED' " +
            "AND EXTRACT(MONTH FROM o.date) = :month " +
            "AND EXTRACT(YEAR FROM o.date) = :year " +
            "GROUP BY DATE(o.date), b.bookid, b.book_name, b.authorid, b.url, b.cover_image " +
            "ORDER BY total_revenue DESC", nativeQuery = true)
    List<Object[]> findRevenueByMonth(@Param("month") int month, @Param("year") int year);
    @Query(value = "SELECT EXTRACT(YEAR FROM o.date) AS order_year, " +
            "b.bookid AS id, " +
            "b.book_name AS book_name, " +
            "b.authorid AS author, " +
            "b.url AS url, " +
            "b.cover_image AS cover_image, " +
            "SUM(od.quantity * b.price) AS total_revenue " +
            "FROM public.order_details od " +
            "JOIN public.books b ON od.bookid = b.bookid " +
            "JOIN public.orders o ON od.orderid = o.orderid " +
            "WHERE o.payment_status = 'COMPLETED' " +
            "AND EXTRACT(YEAR FROM o.date) = :year " +
            "GROUP BY EXTRACT(YEAR FROM o.date), b.bookid, b.book_name, b.authorid, b.url, b.cover_image " +
            "ORDER BY total_revenue DESC", nativeQuery = true)
    List<Object[]> findRevenueByYear(@Param("year") int year);

    List<Book> findAllByCategory_CateNameAndBookTypeAndIsVisible(String category, BookType bookType,boolean isVisible);
}
